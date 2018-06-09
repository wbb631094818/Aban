/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.compose

import android.net.Uri
import android.support.v13.view.inputmethod.InputContentInfoCompat
import common.base.AbanView
import io.reactivex.Observable
import io.reactivex.subjects.Subject
import model.Contact
import model.Message

interface ComposeView : AbanView<ComposeState> {

    val activityVisibleIntent: Observable<Boolean>
    val queryChangedIntent: Observable<CharSequence>
    val queryBackspaceIntent: Observable<*>
    val queryEditorActionIntent: Observable<Int>
    val chipSelectedIntent: Subject<Contact>
    val chipDeletedIntent: Subject<Contact>
    val menuReadyIntent: Observable<Unit>
    val optionsItemIntent: Observable<Int>
    val messageClickIntent: Subject<Message>
    val messagesSelectedIntent: Observable<List<Long>>
    val cancelSendingIntent: Subject<Message>
    val attachmentDeletedIntent: Subject<Attachment>
    val textChangedIntent: Observable<CharSequence>
    val attachIntent: Observable<Unit>
    val cameraIntent: Observable<*>
    val galleryIntent: Observable<*>
    val attachmentSelectedIntent: Observable<Uri>
    val inputContentIntent: Observable<InputContentInfoCompat>
    val sendIntent: Observable<Unit>

    fun clearSelection()
    fun requestStoragePermission()
    fun requestCamera()
    fun requestGallery()
    fun setDraft(draft: String)
    fun scrollToMessage(id: Long)
    val backPressedIntent: Observable<Unit>

}