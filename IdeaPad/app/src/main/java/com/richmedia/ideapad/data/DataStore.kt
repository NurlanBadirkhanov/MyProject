package com.richmedia.ideapad.data

import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DataStore {

    @JvmStatic
    lateinit var notes: NoteDatabase
        private set

    fun init(context: Context) {
        notes = NoteDatabase(context)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun execute(fn: Runnable) {
        GlobalScope.launch(Dispatchers.IO) {
            fn.run()
        }
    }
}
