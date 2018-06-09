/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.reply

import common.base.AbanView
import io.reactivex.Observable

interface AbanReplyView : AbanView<AbanReplyState> {

    val menuItemIntent: Observable<Int>
    val textChangedIntent: Observable<CharSequence>
    val sendIntent: Observable<Unit>

    fun setDraft(draft: String)
    fun finish()

}