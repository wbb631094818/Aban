/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.widget

import android.content.Context
import android.support.text.emoji.widget.EmojiAppCompatTextView
import android.util.AttributeSet
import common.util.TextViewStyler
import injection.appComponent
import javax.inject.Inject

open class AbanTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : EmojiAppCompatTextView(context, attrs) {

    @Inject lateinit var textViewStyler: TextViewStyler

    init {
        if (!isInEditMode) {
            appComponent.inject(this)
            textViewStyler.applyAttributes(this, attrs)
        }
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        setLinkTextColor(color)
    }

}