/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.util.extensions

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

fun RecyclerView.Adapter<*>.autoScrollToStart(recyclerView: RecyclerView) {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            if (layoutManager.stackFromEnd) {
                if (positionStart > 0) {
                    notifyItemChanged(positionStart - 1)
                }

                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (positionStart >= getItemCount() - 1 && lastPosition == positionStart - 1) {
                    recyclerView.scrollToPosition(positionStart)
                }
            } else {
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                if (firstVisiblePosition == 0) {
                    recyclerView.scrollToPosition(positionStart)
                }
            }
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            if (!layoutManager.stackFromEnd) {
                onItemRangeInserted(positionStart, itemCount)
            }
        }
    })
}