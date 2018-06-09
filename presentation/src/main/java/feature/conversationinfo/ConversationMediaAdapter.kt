/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.conversationinfo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.hatamiarash.aban.R
import common.base.AbanRealmAdapter
import common.base.AbanViewHolder
import common.util.GlideApp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.conversation_media_list_item.view.*
import model.MmsPart
import javax.inject.Inject

class ConversationMediaAdapter @Inject constructor(
        private val context: Context
) : AbanRealmAdapter<MmsPart>() {

    val thumbnailClicks: PublishSubject<View> = PublishSubject.create()

    private val disposables = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_media_list_item, parent, false)
        return AbanViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val part = getItem(position)!!
        val view = holder.itemView

        GlideApp.with(context)
                .load(part.getUri())
                .fitCenter()
                .into(view.thumbnail)

        view.thumbnail.transitionName = part.id.toString()
        view.thumbnail.setOnClickListener { thumbnailClicks.onNext(it) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposables.clear()
    }

}