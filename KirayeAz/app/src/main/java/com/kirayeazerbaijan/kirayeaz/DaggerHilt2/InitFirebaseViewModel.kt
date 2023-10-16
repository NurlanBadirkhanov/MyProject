package com.kirayeazerbaijan.kirayeaz.DaggerHilt2


import android.util.Log

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitFirebaseViewModel
    @Inject constructor() : ViewModel() {
    @Inject
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance() // Инициализируем FirebaseAuth в конструкторе
    }

    fun initDatabase() {
        database.setPersistenceEnabled(true)
        Log.d("MyLog", "Database initialized: $database")
    }

    fun initAuth() {

        Log.d("MyLog", "Auth initialized: $auth")
    }
}

