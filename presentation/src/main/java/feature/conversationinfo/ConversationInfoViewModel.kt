/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.conversationinfo

import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.Navigator
import common.base.AbanViewModel
import interactor.DeleteConversations
import interactor.MarkArchived
import interactor.MarkBlocked
import interactor.MarkUnarchived
import interactor.MarkUnblocked
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import model.Conversation
import repository.MessageRepository
import util.extensions.asObservable
import javax.inject.Inject
import javax.inject.Named

class ConversationInfoViewModel @Inject constructor(
        @Named("threadId") threadId: Long,
        messageRepo: MessageRepository,
        private val markArchived: MarkArchived,
        private val markUnarchived: MarkUnarchived,
        private val markBlocked: MarkBlocked,
        private val markUnblocked: MarkUnblocked,
        private val navigator: Navigator,
        private val deleteConversations: DeleteConversations
) : AbanViewModel<ConversationInfoView, ConversationInfoState>(
        ConversationInfoState(threadId = threadId, media = messageRepo.getPartsForConversation(threadId))
) {

    private val conversation: Observable<Conversation>

    init {
        conversation = messageRepo.getConversationAsync(threadId)
                .asObservable<Conversation>()
                .filter { conversation -> conversation.isLoaded }
                .doOnNext { conversation ->
                    if (!conversation.isValid) {
                        newState { it.copy(hasError = true) }
                    }
                }
                .filter { conversation -> conversation.isValid }
                .filter { conversation -> conversation.id != 0L }

        disposables += markArchived
        disposables += markUnarchived
        disposables += markBlocked
        disposables += markUnblocked
        disposables += deleteConversations

        // Update the recipients whenever they change
        disposables += conversation
                .map { conversation -> conversation.recipients }
                .distinctUntilChanged()
                .subscribe { recipients -> newState { it.copy(recipients = recipients) } }

        // Update the view's archived state whenever it changes
        disposables += conversation
                .map { conversation -> conversation.archived }
                .distinctUntilChanged()
                .subscribe { archived -> newState { it.copy(archived = archived) } }

        // Update the view's blocked state whenever it changes
        disposables += conversation
                .map { conversation -> conversation.blocked }
                .distinctUntilChanged()
                .subscribe { blocked -> newState { it.copy(blocked = blocked) } }
    }

    override fun bindView(view: ConversationInfoView) {
        super.bindView(view)

        // Show the notifications settings for the conversation
        view.notificationsIntent
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .autoDisposable(view.scope())
                .subscribe { conversation -> navigator.showNotificationSettings(conversation.id) }

        // Show the theme settings for the conversation
        view.themeIntent
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .autoDisposable(view.scope())
                .subscribe { conversation -> navigator.showThemePicker(conversation.id) }

        // Toggle the archived state of the conversation
        view.archiveIntent
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .autoDisposable(view.scope())
                .subscribe { conversation ->
                    when (conversation.archived) {
                        true -> markUnarchived.execute(listOf(conversation.id))
                        false -> markArchived.execute(listOf(conversation.id))
                    }
                }

        // Toggle the blocked state of the conversation
        view.blockIntent
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .autoDisposable(view.scope())
                .subscribe { conversation ->
                    when (conversation.blocked) {
                        true -> markUnblocked.execute(listOf(conversation.id))
                        false -> markBlocked.execute(listOf(conversation.id))
                    }
                }

        // Show the delete confirmation dialog
        view.deleteIntent
                .autoDisposable(view.scope())
                .subscribe { view.showDeleteDialog() }

        // Delete the conversation
        view.confirmDeleteIntent
                .withLatestFrom(conversation, { _, conversation -> conversation })
                .autoDisposable(view.scope())
                .subscribe { conversation -> deleteConversations.execute(listOf(conversation.id)) }
    }

}