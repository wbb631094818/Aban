/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.conversationinfo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ir.hatamiarash.aban.R
import common.Navigator
import common.base.AbanRealmAdapter
import common.base.AbanViewHolder
import common.util.extensions.setVisible
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.conversation_recipient_list_item.view.*
import model.Recipient
import javax.inject.Inject

class ConversationRecipientAdapter @Inject constructor(
        private val navigator: Navigator
) : AbanRealmAdapter<Recipient>() {

    var threadId: Long = 0L

    private val disposables = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.conversation_recipient_list_item, parent, false)
        return AbanViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val recipient = getItem(position)!!
        val view = holder.itemView

        view.setOnClickListener {
            if (recipient.contact == null) {
                navigator.addContact(recipient.address)
            } else {
                view.avatar.callOnClick()
            }
        }

        view.avatar.threadId = threadId
        view.avatar.setContact(recipient)

        view.name.text = recipient.contact?.name ?: recipient.address

        view.address.text = recipient.address
        view.address.setVisible(recipient.contact != null)

        view.add.setVisible(recipient.contact == null)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposables.clear()
    }

}