/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.blocked

import android.view.LayoutInflater
import android.view.ViewGroup
import ir.hatamiarash.aban.R
import common.base.AbanRealmAdapter
import common.base.AbanViewHolder
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.blocked_list_item.view.*
import model.Conversation
import javax.inject.Inject

class BlockedAdapter @Inject constructor() : AbanRealmAdapter<Conversation>() {

    val unblock: PublishSubject<Long> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        return AbanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blocked_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val conversation = getItem(position)!!
        val view = holder.itemView

        view.setOnClickListener { unblock.onNext(conversation.id) }

        view.avatars.contacts = conversation.recipients
        view.title.text = conversation.getTitle()
    }

}