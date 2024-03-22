package com.richmedia.morningbell

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.richmedia.morningbell.databinding.ActivityAlarmOnBinding

class AlarmOnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmOnBinding
    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAlarmOnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mp = MediaPlayer.create(applicationContext, uri)
        mp.isLooping = true
        mp.start()

        binding.btnStopAlarm.setOnClickListener {
            mp.stop()
            mp.release()
            finish()
        }
    }
}
