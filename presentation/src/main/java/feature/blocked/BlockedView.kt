/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.blocked

import common.base.AbanView
import io.reactivex.Observable

interface BlockedView : AbanView<BlockedState> {

    val siaClickedIntent: Observable<*>
    val unblockIntent: Observable<Long>
    val confirmUnblockIntent: Observable<*>

    fun showUnblockDialog()

}