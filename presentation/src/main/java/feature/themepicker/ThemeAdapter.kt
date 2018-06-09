/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.themepicker

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import ir.hatamiarash.aban.R
import common.base.AbanAdapter
import common.base.AbanViewHolder
import common.util.Colors
import common.util.extensions.dpToPx
import common.util.extensions.setBackgroundTint
import common.util.extensions.setTint
import common.util.extensions.setVisible
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.theme_list_item.view.*
import kotlinx.android.synthetic.main.theme_palette_list_item.view.*
import javax.inject.Inject

class ThemeAdapter @Inject constructor(
        private val context: Context,
        private val colors: Colors
) : AbanAdapter<List<Int>>() {

    val colorSelected: Subject<Int> = PublishSubject.create()

    var selectedColor: Int = -1
        set(value) {
            val oldPosition = data.indexOfFirst { it.contains(field) }
            val newPosition = data.indexOfFirst { it.contains(value) }

            field = value
            iconTint = colors.textPrimaryOnThemeForColor(value)

            oldPosition.takeIf { it != -1 }?.let { position -> notifyItemChanged(position) }
            newPosition.takeIf { it != -1 }?.let { position -> notifyItemChanged(position) }
        }

    private var iconTint = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.theme_palette_list_item, parent, false)
        view.palette.flexWrap = FlexWrap.WRAP
        view.palette.flexDirection = FlexDirection.ROW

        return AbanViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbanViewHolder, position: Int) {
        val palette = getItem(position)
        val view = holder.itemView

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val minPadding = (16 * 6).dpToPx(context)
        val size = if (screenWidth - minPadding > (56 * 5).dpToPx(context)) {
            56.dpToPx(context)
        } else {
            (screenWidth - minPadding) / 5
        }
        val swatchPadding = (screenWidth - size * 5) / 12

        view.palette.removeAllViews()
        view.palette.setPadding(swatchPadding, swatchPadding, swatchPadding, swatchPadding)

        (palette.subList(0, 5) + palette.subList(5, 10).reversed())
                .mapIndexed { index, color ->
                    LayoutInflater.from(context).inflate(R.layout.theme_list_item, view.palette, false).apply {

                        // Send clicks to the selected subject
                        setOnClickListener { colorSelected.onNext(color) }

                        // Apply the color to the view
                        theme.setBackgroundTint(color)

                        // Control the check visibility and tint
                        check.setVisible(color == selectedColor)
                        check.setTint(iconTint)

                        // Update the size so that the spacing is perfectly even
                        layoutParams = (layoutParams as FlexboxLayout.LayoutParams).apply {
                            height = size
                            width = size
                            isWrapBefore = index % 5 == 0
                            setMargins(swatchPadding, swatchPadding, swatchPadding, swatchPadding)
                        }
                    }
                }
                .forEach { theme -> view.palette.addView(theme) }
    }

}