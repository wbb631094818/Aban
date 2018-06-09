/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.conversationinfo

import io.reactivex.Observable
import common.base.AbanView

interface ConversationInfoView : AbanView<ConversationInfoState> {

    val notificationsIntent: Observable<Unit>
    val themeIntent: Observable<Unit>
    val archiveIntent: Observable<Unit>
    val blockIntent: Observable<Unit>
    val deleteIntent: Observable<Unit>
    val confirmDeleteIntent: Observable<Unit>

    fun showDeleteDialog()

}