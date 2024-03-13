package com.neco_desarrollo.shoppinglist.activities

import android.app.Application
import com.neco_desarrollo.shoppinglist.db.MainDataBase

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
}