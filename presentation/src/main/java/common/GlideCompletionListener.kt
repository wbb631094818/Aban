/*
 * Copyright (c) 2018. Arash Hatami
 */
package common

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlideCompletionListener<T>(private val listener: () -> Unit) : RequestListener<T> {

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<T>?, isFirstResource: Boolean): Boolean {
        listener()
        return false
    }

    override fun onResourceReady(resource: T, model: Any?, target: Target<T>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        listener()
        return false
    }

}