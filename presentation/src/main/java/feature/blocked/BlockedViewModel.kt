/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.blocked

import android.content.Context
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.Navigator
import common.base.AbanViewModel
import interactor.MarkUnblocked
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import repository.MessageRepository
import util.Preferences
import util.tryOrNull
import javax.inject.Inject

class BlockedViewModel @Inject constructor(
        private val context: Context,
        private val markUnblocked: MarkUnblocked,
        private val messageRepo: MessageRepository,
        private val navigator: Navigator,
        private val prefs: Preferences
) : AbanViewModel<BlockedView, BlockedState>(BlockedState()) {

    init {
        newState { it.copy(data = messageRepo.getBlockedConversations()) }

        disposables += prefs.sia.asObservable()
                .subscribe { enabled -> newState { it.copy(siaEnabled = enabled) } }
    }

    override fun bindView(view: BlockedView) {
        super.bindView(view)

        view.siaClickedIntent
                .map {
                    tryOrNull { context.packageManager.getApplicationInfo("org.mistergroup.shouldianswerpersonal", 0).enabled }
                            ?: tryOrNull { context.packageManager.getApplicationInfo("org.mistergroup.muzutozvednout", 0).enabled }
                            ?: false
                }
                .doOnNext { installed -> if (!installed) navigator.showSia() }
                .withLatestFrom(prefs.sia.asObservable(), { installed, enabled -> installed && !enabled })
                .autoDisposable(view.scope())
                .subscribe { shouldEnable -> prefs.sia.set(shouldEnable) }

        // Show confirm unblock conversation dialog
        view.unblockIntent
                .autoDisposable(view.scope())
                .subscribe { view.showUnblockDialog() }

        // Unblock conversation
        view.confirmUnblockIntent
                .withLatestFrom(view.unblockIntent, { _, threadId -> threadId })
                .autoDisposable(view.scope())
                .subscribe { threadId -> markUnblocked.execute(listOf(threadId)) }
    }

}