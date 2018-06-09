/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.gallery

import android.content.Context
import android.content.Intent
import ir.hatamiarash.aban.R
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.base.AbanViewModel
import common.util.extensions.makeToast
import interactor.SaveImage
import io.reactivex.Flowable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import repository.MessageRepository
import util.extensions.mapNotNull
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
        private val intent: Intent,
        private val context: Context,
        private val messageRepo: MessageRepository,
        private val saveImage: SaveImage
) : AbanViewModel<GalleryView, GalleryState>(GalleryState()) {

    private val partIdFlowable = Flowable.just(intent)
            .map { it.getLongExtra("partId", 0L) }
            .filter { partId -> partId != 0L }

    init {
        disposables += partIdFlowable
                .mapNotNull { partId -> messageRepo.getPart(partId) }
                .mapNotNull { part -> part.getUri() }
                .subscribe { uri -> newState { it.copy(imageUri = uri) } }

        disposables += partIdFlowable
                .mapNotNull { partId -> messageRepo.getMessageForPart(partId) }
                .mapNotNull { message -> messageRepo.getConversation(message.threadId) }
                .subscribe { conversation -> newState { it.copy(title = conversation.getTitle()) } }
    }

    override fun bindView(view: GalleryView) {
        super.bindView(view)

        // When the screen is touched, toggle the visibility of the navigation UI
        view.screenTouchedIntent
                .withLatestFrom(state, { _, state -> state.navigationVisible })
                .map { navigationVisible -> !navigationVisible }
                .autoDisposable(view.scope())
                .subscribe { navigationVisible -> newState { it.copy(navigationVisible = navigationVisible) } }

        // Save image to device
        view.optionsItemSelectedIntent
                .filter { itemId -> itemId == R.id.save }
                .withLatestFrom(partIdFlowable.toObservable(), { _, partId -> partId })
                .autoDisposable(view.scope())
                .subscribe { partId -> saveImage.execute(partId) { context.makeToast(R.string.gallery_toast_saved) } }
    }

}