/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.compose

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.base.AbanAdapter
import common.base.AbanViewHolder
import common.util.extensions.setVisible
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.contact_list_item.view.*
import model.Contact
import javax.inject.Inject

class ContactAdapter @Inject constructor() : AbanAdapter<Contact>() {

    val contactSelected: Subject<Contact> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.contact_list_item, parent, false)
        return AbanViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val contact = getItem(position)
        val view = holder.itemView

        view.primary.clicks().subscribe { contactSelected.onNext(copyContact(contact, 0)) }

        view.avatar.setContact(contact)
        view.name.text = contact.name
        view.name.setVisible(view.name.text.isNotEmpty())
        view.address.text = contact.numbers.first()?.address ?: ""
        view.type.text = contact.numbers.first()?.type ?: ""

        view.addresses.removeAllViews()
        contact.numbers.forEachIndexed { index, number ->
            if (index != 0) {
                val numberView = View.inflate(view.context, R.layout.contact_number_list_item, null)
                numberView.clicks().subscribe { contactSelected.onNext(copyContact(contact, index)) }
                numberView.address.text = number.address
                numberView.type.text = number.type
                view.addresses.addView(numberView)
            }
        }

    }

    /**
     * Creates a copy of the contact with only one phone number, so that the chips
     * view can still display the name/photo, and not get confused about which phone number to use
     */
    private fun copyContact(contact: Contact, numberIndex: Int) = Contact().apply {
        lookupKey = contact.lookupKey
        name = contact.name
        numbers.add(contact.numbers[numberIndex])
    }

    override fun areItemsTheSame(old: Contact, new: Contact): Boolean {
        return old.lookupKey == new.lookupKey
    }

}