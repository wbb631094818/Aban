/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.conversations

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import ir.hatamiarash.aban.R
import common.Navigator
import common.base.AbanRealmAdapter
import common.base.AbanViewHolder
import common.util.DateFormatter
import common.util.extensions.resolveThemeColor
import kotlinx.android.synthetic.main.conversation_list_item.view.*
import model.Conversation
import javax.inject.Inject

class ConversationsAdapter @Inject constructor(
        private val context: Context,
        private val dateFormatter: DateFormatter,
        private val navigator: Navigator
) : AbanRealmAdapter<Conversation>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.conversation_list_item, parent, false)

        if (viewType == 1) {
            val textColorPrimary = parent.context.resolveThemeColor(android.R.attr.textColorPrimary)

            view.title.setTypeface(view.title.typeface, Typeface.BOLD)

            view.snippet.setTypeface(view.snippet.typeface, Typeface.BOLD)
            view.snippet.setTextColor(textColorPrimary)
            view.snippet.maxLines = 5

            view.date.setTypeface(view.date.typeface, Typeface.BOLD)
            view.date.setTextColor(textColorPrimary)
        }

        return AbanViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: AbanViewHolder, position: Int) {
        val conversation = getItem(position)!!
        val view = viewHolder.itemView

        view.setOnClickListener {
            when (toggleSelection(conversation.id, false)) {
                true -> view.isSelected = isSelected(conversation.id)
                false -> navigator.showConversation(conversation.id)
            }
        }
        view.setOnLongClickListener {
            toggleSelection(conversation.id)
            view.isSelected = isSelected(conversation.id)
            true
        }

        view.isSelected = isSelected(conversation.id)

        view.avatars.contacts = conversation.recipients
        view.title.text = conversation.getTitle()
        view.date.text = dateFormatter.getConversationTimestamp(conversation.date)
        view.snippet.text = when (conversation.me) {
            true -> context.getString(R.string.main_sender_you, conversation.snippet)
            false -> conversation.snippet
        }
    }

    override fun getItemId(index: Int): Long {
        return getItem(index)!!.id
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)!!.read) 0 else 1
    }
}