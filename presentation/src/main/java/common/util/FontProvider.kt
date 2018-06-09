/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.util

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import ir.hatamiarash.aban.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FontProvider @Inject constructor(context: Context) {

    private var lato: Typeface? = null
    private val pendingCallbacks = ArrayList<(Typeface) -> Unit>()

    init {
        ResourcesCompat.getFont(context, R.font.lato, object : ResourcesCompat.FontCallback() {
            override fun onFontRetrievalFailed(reason: Int) {
                Timber.w("Font retrieval failed: $reason")
            }

            override fun onFontRetrieved(typeface: Typeface) {
                lato = typeface

                pendingCallbacks.forEach { lato?.run(it) }
                pendingCallbacks.clear()
            }
        }, null)
    }

    fun getLato(callback: (Typeface) -> Unit) {
        lato?.run(callback) ?: pendingCallbacks.add(callback)
    }

}