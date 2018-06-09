/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.main

import io.realm.RealmResults
import model.Conversation
import model.SearchResult
import repository.SyncRepository

data class MainState(
        val hasError: Boolean = false,
        val page: MainPage = Inbox(),
        val drawerOpen: Boolean = false,
        val showRating: Boolean = false,
        val syncing: SyncRepository.SyncProgress = SyncRepository.SyncProgress.Idle(),
        val defaultSms: Boolean = false,
        val smsPermission: Boolean = false,
        val contactPermission: Boolean = false
)

sealed class MainPage

data class Inbox(
        val showClearButton: Boolean = false,
        val data: RealmResults<Conversation>? = null,
        val selected: Int = 0,
        val showArchivedSnackbar: Boolean = false) : MainPage()

data class Searching(
        val loading: Boolean = false,
        val data: List<SearchResult>? = null
) : MainPage()

data class Archived(
        val showClearButton: Boolean = false,
        val data: RealmResults<Conversation>? = null,
        val selected: Int = 0) : MainPage()

data class Scheduled(
        val data: Any? = null) : MainPage()