/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.compose

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.base.AbanAdapter
import common.base.AbanViewHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.attachment_list_item.view.*
import javax.inject.Inject

class AttachmentAdapter @Inject constructor(
        private val context: Context
) : AbanAdapter<Attachment>() {

    val attachmentDeleted: Subject<Attachment> = PublishSubject.create()

    private val disposables = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attachment_list_item, parent, false)

        view.thumbnailBounds.clipToOutline = true

        return AbanViewHolder(view)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposables.clear()
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val attachment = getItem(position)
        val view = holder.itemView

        view.clicks().subscribe {
            attachmentDeleted.onNext(attachment)
        }

        Glide.with(context).load(attachment.getUri()).into(view.thumbnail)
    }

}