package com.richmedia.glowguide

import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var cameraManager: CameraManager? = null
    private var cameraId: String? = null
    private var isFlashlightOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager?
        try {
            cameraId = cameraManager?.cameraIdList?.get(0)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        val flashlightOn = findViewById<ImageView>(R.id.im_on)
        val flashlightOff = findViewById<ImageView>(R.id.im_off)


        flashlightOn.setOnClickListener {
            flashlightOn.visibility = View.GONE // Скрываем кнопку для включенного состояния
            toggleFlashlight()
        }

        flashlightOff.setOnClickListener {
            flashlightOn.visibility = View.VISIBLE // Показываем кнопку для включенного состояния
            toggleFlashlight()
        }

    }

    private fun toggleFlashlight() {
        if (isFlashlightOn) {
            turnOffFlashlight()
        } else {
            turnOnFlashlight()
        }
    }

    private fun turnOnFlashlight() {
        try {
            cameraManager?.setTorchMode(cameraId!!, true)
            isFlashlightOn = true
            Toast.makeText(this, "Фонарик включен", Toast.LENGTH_SHORT).show()
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun turnOffFlashlight() {
        try {
            cameraManager?.setTorchMode(cameraId!!, false)
            isFlashlightOn = false
            Toast.makeText(this, "Фонарик выключен", Toast.LENGTH_SHORT).show()
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isFlashlightOn) {
            turnOffFlashlight()
        }
    }
}