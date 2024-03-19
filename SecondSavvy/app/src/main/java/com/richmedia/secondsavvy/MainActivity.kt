package com.richmedia.secondsavvy
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var seconds = 0
    private var running = false
    private lateinit var notificationManager: NotificationManagerCompat
    private val notificationId = 1001
    private val timeList = mutableListOf<String>()
    private lateinit var adapter:TimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = TimeAdapter(timeList)
        recyclerView.adapter = adapter
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
        }

        runTimer()

        val startButton: Button = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            running = true
        }

        val stopButton: Button = findViewById(R.id.stop_button)
        stopButton.setOnClickListener {
            running = false
        }

        val resetButton: Button = findViewById(R.id.reset_button)
        resetButton.setOnClickListener {
            running = false
            seconds = 0
        }

        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
            saveTime()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("seconds", seconds)
        savedInstanceState.putBoolean("running", running)
    }

    private fun runTimer() {
        val timeView: TextView = findViewById(R.id.time_view)

        val handler = Handler()

        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val msec = seconds % 60

                val time = String.format("%d:%02d:%02d", hours, minutes,msec)
                timeView.text = time

                if (running) {
                    seconds++
                    showNotification(time)
                }

                handler.postDelayed(this, 10)
            }
        })
    }

    private fun showNotification(time: String) {
        val builder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Секундомер")
            .setContentText("Текущее время: $time")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.VIBRATE),
                1
            )
            return

        }
        notificationManager.notify(notificationId, builder.build())
    }

    private fun saveTime() {
        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        val secs = seconds % 60

        val time = String.format("%d:%02d:%02d", hours, minutes, secs)

        // Добавляем текст "Результат" в список перед временем
        timeList.add("Результат: $time")

        // Обновляем адаптер после добавления нового времени в список
        adapter.notifyDataSetChanged()

        // Здесь вы можете выполнить дополнительные действия, например, обновить пользовательский интерфейс или сохранить список в базу данных
    }


}
