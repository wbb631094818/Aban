/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.gallery

import io.reactivex.Observable
import common.base.AbanView

interface GalleryView : AbanView<GalleryState> {

    val screenTouchedIntent: Observable<Unit>
    val optionsItemSelectedIntent: Observable<Int>

}