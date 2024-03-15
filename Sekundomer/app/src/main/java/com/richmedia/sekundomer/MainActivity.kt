package com.richmedia.sekundomer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.richmedia.sekundomer.R
import com.richmedia.sekundomer.adapter.LapAdapter
import com.richmedia.sekundomer.databinding.ActivityMainBinding
import com.richmedia.sekundomer.model.Lap
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    private var running: Boolean = false
    private var increment = 0
    private var curIncrement = 0
    private var prevIncrement = 0
    private var incrementLap: Int by Delegates.observable(0) { _, oldValue, newValue ->
        curIncrement = newValue
        prevIncrement = oldValue
    }

    private var indexLap = 0
    private var listLap: MutableList<Lap> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.saTime.progress = 0
        binding.saTime.isEnabled = false

        binding.btnPause.isEnabled = false
        binding.btnLapReset.isEnabled = false

        binding.btnTime.setOnClickListener(this)
        binding.btnPause.setOnClickListener(this)
        binding.btnLapReset.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_time -> startStopwatch()
            R.id.btnPause -> pauseStopwatch()
            R.id.btn_lap_reset -> lapOrResetStopWatch()
        }
    }

    private fun startStopwatch() {
        binding.btnPause.isEnabled = true
        binding.btnLapReset.isEnabled = true
        binding.btnLapReset.text = resources.getString(R.string.lap)

        // jika state kondisi false atau pause, untuk menghindari start yang berulang" jika user menekan tombol start
        if (!running) {
            running = true
            CoroutineScope(IO).launch {
                while (running) {
                    delay(10)

                    // cast anyone to double to get result double
                    val second = (increment / 90.0) % 60
                    val progress = (second / 60) * 100

                    withContext(Main) {
                        binding.saTime.progress = progress.toInt()
                        binding.btnTime.text = Lap.convertToDuration(increment)
                    }

                    increment += 1
                }
            }
        }
    }

    private fun pauseStopwatch() {
        running = false
        binding.btnLapReset.text = resources.getString(R.string.reset)
    }

    private fun lapOrResetStopWatch() {
        if (running) {
            incrementLap = increment
            val diff = curIncrement - prevIncrement

            val lap = Lap(indexLap, increment, diff)
            listLap.add(lap)

            val lapAdapter = LapAdapter(this, listLap)
            binding.lvLapResult.adapter = lapAdapter

            indexLap += 1
        } else {
            increment = 0
            incrementLap = 0
            indexLap = 0
            listLap.clear()

            binding.saTime.progress = 0
            binding.lvLapResult.adapter = null

            binding.btnPause.isEnabled = false
            binding.btnLapReset.isEnabled = false
            binding.btnTime.text = resources.getString(R.string.start)
        }
    }
}