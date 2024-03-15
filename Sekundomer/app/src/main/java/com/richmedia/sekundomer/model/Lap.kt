package com.richmedia.sekundomer.model

import java.util.*


data class Lap(
        var index: Int,
        var lap: Int,
        var diff: Int
) {
    companion object {
        fun convertToDuration(increment: Int): String {
            val minute = (increment / 90) / 60
            val second = (increment / 90) % 60
            val millis = (increment % 90)

            return String.format(
                    Locale.US,
                    "%02d:%02d:%02d",
                    minute, second, millis
            )
        }
    }
}