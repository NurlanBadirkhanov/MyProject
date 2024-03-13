package com.neco_desarrollo.shoppinglist.utils

import android.content.SharedPreferences
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    const val DEF_TIME_FORMAT = "hh:mm:ss - yyyy/MM/dd"
    fun getCurrentTime(): String{
        val formatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun getTimeFormat(time: String, defPreferences: SharedPreferences): String{
        val defFormatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val newFormat = defPreferences.getString("time_format_key", DEF_TIME_FORMAT)
        Log.d("MyLog","New format: $newFormat")
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
        return if(defDate != null){
            newFormatter.format(defDate)
        } else {
            time
        }
    }
}