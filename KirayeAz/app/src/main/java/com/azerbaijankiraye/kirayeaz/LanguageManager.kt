package com.azerbaijankiraye.kirayeaz

import android.content.Context
import java.util.*

class LanguageManager(private val context: Context) {
    private val prefsKey = "MyPrefs"

    fun setLocale() {
        val language = getSavedLanguage()
        val locale = if (language == "ru")
            Locale("az") else Locale("ru")
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun getSavedLanguage(): String {
        return context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)
            .getString("language", "") ?: Locale.getDefault().language
    }

    fun saveLanguage(language: String) {
        context.getSharedPreferences(prefsKey, Context.MODE_PRIVATE)
            .edit()
            .putString("language", language)
            .apply()
    }
}




