/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.main

import common.base.AbanView
import io.reactivex.Observable

interface MainView : AbanView<MainState> {

    val activityResumedIntent: Observable<*>
    val queryChangedIntent: Observable<CharSequence>
    val composeIntent: Observable<Unit>
    val drawerOpenIntent: Observable<Boolean>
    val homeIntent: Observable<*>
    val drawerItemIntent: Observable<DrawerItem>
    val optionsItemIntent: Observable<Int>
    val dismissRatingIntent: Observable<*>
    val rateIntent: Observable<*>
    val conversationsSelectedIntent: Observable<List<Long>>
    val confirmDeleteIntent: Observable<Unit>
    val swipeConversationIntent: Observable<Long>
    val undoSwipeConversationIntent: Observable<Unit>
    val snackbarButtonIntent: Observable<Unit>
    val backPressedIntent: Observable<Unit>

    fun requestPermissions()
    fun clearSearch()
    fun clearSelection()
    fun showDeleteDialog()

}

enum class DrawerItem { INBOX, ARCHIVED, SCHEDULED, BLOCKED, SETTINGS, PLUS, HELP }