/*
 * Copyright (c) 2018. Arash Hatami
 */

package repository

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    fun loadImage(uri: Uri): Bitmap?

    fun saveImage(uri: Uri)

}