/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.gallery

import android.net.Uri

data class GalleryState(
        val navigationVisible: Boolean = true,
        val title: String = "",
        val imageUri: Uri? = null
)