/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.util

import android.content.Context
import android.text.format.DateFormat
import common.util.extensions.isSameDay
import common.util.extensions.isSameWeek
import common.util.extensions.isSameYear
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateFormatter @Inject constructor(val context: Context) {

    /**
     * Replace 12 hour format with 24 hour format if necessary
     */
    private fun getFormatter(pattern: String): SimpleDateFormat {
        val isUsing24HourTime = DateFormat.is24HourFormat(context)

        return if (isUsing24HourTime) SimpleDateFormat(pattern.replace("h", "HH").replace(" a".toRegex(), ""), Locale.getDefault())
        else SimpleDateFormat(pattern, Locale.getDefault())
    }

    fun getTimestamp(date: Long): String {
        return getFormatter("h:mm a").format(date)
    }

    fun getMessageTimestamp(date: Long): String {
        val now = Calendar.getInstance()
        val then = Calendar.getInstance()
        then.timeInMillis = date

        return when {
            now.isSameDay(then) -> getFormatter("h:mm a")
            now.isSameWeek(then) -> getFormatter("E h:mm a")
            now.isSameYear(then) -> getFormatter("MMM d, h:mm a")
            else -> getFormatter("MMM d yyyy, h:mm a")
        }.format(date)
    }

    fun getConversationTimestamp(date: Long): String {
        val now = Calendar.getInstance()
        val then = Calendar.getInstance()
        then.timeInMillis = date

        return when {
            now.isSameDay(then) -> getFormatter("h:mm a")
            now.isSameWeek(then) -> getFormatter("E")
            now.isSameYear(then) -> getFormatter("MMM d")
            else -> getFormatter("d/MM/yy")
        }.format(date)
    }

}