/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.reply

import android.content.Intent
import android.telephony.SmsMessage
import ir.hatamiarash.aban.R
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.Navigator
import common.base.AbanViewModel
import interactor.MarkRead
import interactor.SendMessage
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.realm.RealmResults
import model.Conversation
import model.Message
import repository.MessageRepository
import util.extensions.asObservable
import util.extensions.mapNotNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AbanReplyViewModel @Inject constructor(
        private val intent: Intent,
        private val markRead: MarkRead,
        private val messageRepo: MessageRepository,
        private val navigator: Navigator,
        private val sendMessage: SendMessage
) : AbanViewModel<AbanReplyView, AbanReplyState>(AbanReplyState()) {

    private val conversation by lazy {
        messageRepo.getConversationAsync(intent.getLongExtra("threadId", -1))
                .asObservable<Conversation>()
                .filter { it.isLoaded }
                .filter { it.isValid }
                .distinctUntilChanged()
    }

    private val messages: Subject<RealmResults<Message>> = PublishSubject.create()

    init {
        disposables += markRead
        disposables += sendMessage

        // When the set of messages changes, update the state
        // If we're ever showing an empty set of messages, then it's time to shut down to activity
        disposables += messages
                .withLatestFrom(conversation, { messages, conversation -> Pair(conversation, messages) })
                .doOnNext { data -> newState { it.copy(data = data) } }
                .map { data -> data.second }
                .switchMap { messages -> messages.asObservable() }
                .filter { it.isLoaded }
                .filter { it.isValid }
                .filter { it.isEmpty() }
                .subscribe { newState { it.copy(hasError = true) } }

        disposables += conversation
                .doOnNext { conversation -> newState { it.copy(title = conversation.getTitle()) } }
                .distinctUntilChanged { conversation -> conversation.id } // We only need to set the messages once
                .map { conversation -> messageRepo.getUnreadMessages(conversation.id) }
                .subscribe { messages.onNext(it) }
    }

    override fun bindView(view: AbanReplyView) {
        super.bindView(view)

        conversation
                .map { conversation -> conversation.draft }
                .distinctUntilChanged()
                .autoDisposable(view.scope())
                .subscribe { draft -> view.setDraft(draft) }

        // Mark read
        view.menuItemIntent
                .filter { id -> id == R.id.read }
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .map { conversation -> conversation.id }
                .autoDisposable(view.scope())
                .subscribe { threadId ->
                    markRead.execute(threadId) { newState { it.copy(hasError = true) } }
                }

        // Call
        view.menuItemIntent
                .filter { id -> id == R.id.call }
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .mapNotNull { conversation -> conversation.recipients.first()?.address }
                .doOnNext { address -> navigator.makePhoneCall(address) }
                .autoDisposable(view.scope())
                .subscribe { newState { it.copy(hasError = true) } }

        // Show all messages
        view.menuItemIntent
                .filter { id -> id == R.id.expand }
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .map { conversation -> messageRepo.getMessages(conversation.id) }
                .doOnNext { messages.onNext(it) }
                .autoDisposable(view.scope())
                .subscribe { newState { it.copy(expanded = true) } }

        // Show unread messages only
        view.menuItemIntent
                .filter { id -> id == R.id.collapse }
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .map { conversation -> messageRepo.getUnreadMessages(conversation.id) }
                .doOnNext { messages.onNext(it) }
                .autoDisposable(view.scope())
                .subscribe { newState { it.copy(expanded = false) } }

        // View conversation
        view.menuItemIntent
                .filter { id -> id == R.id.view }
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .map { conversation -> conversation.id }
                .doOnNext { threadId -> navigator.showConversation(threadId) }
                .autoDisposable(view.scope())
                .subscribe { newState { it.copy(hasError = true) } }

        // Enable the send button when there is text input into the new message body or there's
        // an attachment, disable otherwise
        view.textChangedIntent
                .map { text -> text.isNotBlank() }
                .autoDisposable(view.scope())
                .subscribe { canSend -> newState { it.copy(canSend = canSend) } }

        // Show the remaining character counter when necessary
        view.textChangedIntent
                .observeOn(Schedulers.computation())
                .map { draft -> SmsMessage.calculateLength(draft, false) }
                .map { array ->
                    val messages = array[0]
                    val remaining = array[2]

                    when {
                        messages <= 1 && remaining > 10 -> ""
                        messages <= 1 && remaining <= 10 -> "$remaining"
                        else -> "$remaining / $messages"
                    }
                }
                .distinctUntilChanged()
                .autoDisposable(view.scope())
                .subscribe { remaining -> newState { it.copy(remaining = remaining) } }

        // Update the draft whenever the text is changed
        view.textChangedIntent
                .debounce(100, TimeUnit.MILLISECONDS)
                .map { draft -> draft.toString() }
                .observeOn(Schedulers.io())
                .withLatestFrom(conversation.map { it.id }, { draft, threadId ->
                    messageRepo.saveDraft(threadId, draft)
                })
                .autoDisposable(view.scope())
                .subscribe()

        // Send a message when the send button is clicked, and disable editing mode if it's enabled
        view.sendIntent
                .withLatestFrom(view.textChangedIntent, { _, body -> body })
                .map { body -> body.toString() }
                .withLatestFrom(conversation, { body, conversation ->
                    val threadId = conversation.id
                    val addresses = conversation.recipients.map { it.address }

                    view.setDraft("")
                    sendMessage.execute(SendMessage.Params(threadId, addresses, body, listOf())) {
                    }

                    threadId
                })
                .doOnNext { threadId ->
                    markRead.execute(threadId) {
                        newState { it.copy(hasError = true) }
                    }
                }
                .autoDisposable(view.scope())
                .subscribe()
    }

}
