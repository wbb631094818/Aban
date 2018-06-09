/*
 * Copyright (c) 2018. Arash Hatami
 */
package common

import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.ArrayRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.base.AbanAdapter
import common.base.AbanViewHolder
import common.util.Colors
import common.util.extensions.resolveThemeColor
import common.util.extensions.setVisible
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.menu_list_item.view.*
import javax.inject.Inject

data class MenuItem(val title: String, val actionId: Int)

class MenuItemAdapter @Inject constructor(private val context: Context, private val colors: Colors) : AbanAdapter<MenuItem>() {

    val menuItemClicks: Subject<Int> = PublishSubject.create()

    private val disposables = CompositeDisposable()

    var selectedItem: Int? = null
        set(value) {
            val old = data.map { it.actionId }.indexOfFirst { it == field }
            val new = data.map { it.actionId }.indexOfFirst { it == value }

            field = value

            old.let { notifyItemChanged(it) }
            new.let { notifyItemChanged(it) }
        }

    fun setData(@ArrayRes titles: Int, @ArrayRes values: Int = -1) {
        val valueInts = if (values != -1) context.resources.getIntArray(values) else null

        data = context.resources.getStringArray(titles)
                .mapIndexed { index, title -> MenuItem(title, valueInts?.getOrNull(index) ?: index) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.menu_list_item, parent, false)

        val states = arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(-android.R.attr.state_selected))

        val text = parent.context.resolveThemeColor(android.R.attr.textColorTertiary)
        view.check.imageTintList = ColorStateList(states, intArrayOf(colors.theme().theme, text))

        return AbanViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val menuItem = getItem(position)
        val view = holder.itemView

        view.clicks().subscribe { menuItemClicks.onNext(menuItem.actionId) }

        view.title.text = menuItem.title
        view.check.isSelected = (menuItem.actionId == selectedItem)
        view.check.setVisible(selectedItem != null)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposables.clear()
    }

}