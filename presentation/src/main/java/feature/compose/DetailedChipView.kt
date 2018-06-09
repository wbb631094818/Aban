/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.compose

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.RelativeLayout
import ir.hatamiarash.aban.R
import common.util.Colors
import common.util.extensions.setBackgroundTint
import injection.appComponent
import kotlinx.android.synthetic.main.contact_chip_detailed.view.*
import model.Contact
import javax.inject.Inject


class DetailedChipView(context: Context) : RelativeLayout(context) {

    @Inject lateinit var colors: Colors

    init {
        View.inflate(context, R.layout.contact_chip_detailed, this)
        appComponent.inject(this)

        setOnClickListener { hide() }

        visibility = View.GONE

        isFocusable = true
        isFocusableInTouchMode = true

        card.setBackgroundTint(colors.theme().theme)
    }

    fun setContact(contact: Contact) {
        avatar.setContact(contact)
        name.text = contact.name
        info.text = contact.numbers.map { it.address }.toString()
    }

    fun show() {
        startAnimation(AlphaAnimation(0f, 1f).apply { duration = 200 })

        visibility = View.VISIBLE
        requestFocus()
        isClickable = true
    }

    fun hide() {
        startAnimation(AlphaAnimation(1f, 0f).apply { duration = 200 })

        visibility = View.GONE
        clearFocus()
        isClickable = false
    }

    fun setOnDeleteListener(listener: (View) -> Unit) {
        delete.setOnClickListener(listener)
    }

}
