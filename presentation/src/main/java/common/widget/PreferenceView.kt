/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import ir.hatamiarash.aban.R
import common.util.extensions.resolveThemeAttribute
import common.util.extensions.resolveThemeColor
import common.util.extensions.setTint
import common.util.extensions.setVisible
import injection.appComponent
import kotlinx.android.synthetic.main.preference_view.view.*

class PreferenceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayoutCompat(context, attrs) {

    var title: String? = null
        set(value) {
            field = value

            if (isInEditMode) {
                findViewById<TextView>(R.id.titleView).text = value
            } else {
                titleView.text = value
            }
        }

    var summary: String? = null
        set(value) {
            field = value


            if (isInEditMode) {
                findViewById<TextView>(R.id.summaryView).run {
                    text = value
                    setVisible(value?.isNotEmpty() == true)
                }
            } else {
                summaryView.text = value
                summaryView.setVisible(value?.isNotEmpty() == true)
            }
        }

    init {
        if (!isInEditMode) {
            appComponent.inject(this)
        }

        View.inflate(context, R.layout.preference_view, this)
        setBackgroundResource(context.resolveThemeAttribute(R.attr.selectableItemBackground))
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        val textSecondary = context.resolveThemeColor(android.R.attr.textColorSecondary)
        icon.setTint(textSecondary)

        context.obtainStyledAttributes(attrs, R.styleable.PreferenceView)?.run {
            title = getString(R.styleable.PreferenceView_title)
            summary = getString(R.styleable.PreferenceView_summary)

            // If there's a custom view used for the preference's widget, inflate it
            getResourceId(R.styleable.PreferenceView_widget, -1).takeIf { it != -1 }?.let { id ->
                View.inflate(context, id, widgetFrame)
            }

            // If an icon is being used, set up the icon view
            getResourceId(R.styleable.PreferenceView_icon, -1).takeIf { it != -1 }?.let { id ->
                icon.setVisible(true)
                icon.setImageResource(id)
            }

            recycle()
        }
    }

}