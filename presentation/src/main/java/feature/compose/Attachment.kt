/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.compose

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v13.view.inputmethod.InputContentInfoCompat
import com.google.android.mms.ContentType

data class Attachment(private val uri: Uri? = null, private val inputContent: InputContentInfoCompat? = null) {

    fun getUri(): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            inputContent?.contentUri ?: uri
        } else {
            uri
        }
    }

    fun isGif(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && inputContent != null) {
            inputContent.description.hasMimeType(ContentType.IMAGE_GIF)
        } else {
            context.contentResolver.getType(uri) == ContentType.IMAGE_GIF
        }
    }

}