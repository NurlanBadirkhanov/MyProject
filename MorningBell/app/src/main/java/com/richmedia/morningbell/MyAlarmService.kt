package com.richmedia.morningbell

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class MyAlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Alarm Triggered", Toast.LENGTH_SHORT).show()

        // Здесь можно добавить код для выполнения дополнительных действий при срабатывании будильника

        return START_NOT_STICKY
    }
}
