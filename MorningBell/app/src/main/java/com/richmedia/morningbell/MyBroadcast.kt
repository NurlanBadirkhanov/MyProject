package com.richmedia.morningbell

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = Intent(context, MyAlarmService::class.java)
        context?.startService(service)
    }
}

