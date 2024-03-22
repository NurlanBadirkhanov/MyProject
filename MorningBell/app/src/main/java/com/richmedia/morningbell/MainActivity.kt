package com.richmedia.morningbell



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.richmedia.morningbell.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private var requestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding.btnSavetime.setOnClickListener {

            val cal = Calendar.getInstance()

            val sec = binding.etSetTime // Используйте minutes вместо seconds
            cal.set(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                sec.hour,
                sec.minute, // Минуты, а не секунды
                0
            )

            val i = Intent(applicationContext, MyBroadcast::class.java)
            val pi = PendingIntent.getBroadcast(
                applicationContext,
                requestCode++,
                i,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            Toast.makeText(
                this,
                "Alarm Is set at ${cal.get(Calendar.HOUR_OF_DAY)} : ${cal.get(Calendar.MINUTE)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
