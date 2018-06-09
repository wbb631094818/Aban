/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.themepicker

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import ir.hatamiarash.aban.R
import javax.inject.Inject

class ThemePagerAdapter @Inject constructor(private val context: Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return when (position) {
            1 -> container.findViewById(R.id.hsvPicker)
            else -> container.findViewById(R.id.materialColors)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> context.getString(R.string.theme_plus)
            else -> context.getString(R.string.theme_material)
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 2
    }

}