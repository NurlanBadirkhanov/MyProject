package com.richmedia.ideapad

import android.app.Application
import com.richmedia.ideapad.data.DataStore


class NotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStore.init(this)
    }
}
