package com.ilnodstudio.ansartelecom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.animation.ObjectAnimator
import android.content.Intent
import android.view.WindowManager
import com.ilnodstudio.ansartelecom.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setContentView(R.layout.activity_splash)
        CoroutineScope(Dispatchers.Main).launch {
            binding.progressBar.max=1000
            val value = 1000
            ObjectAnimator.ofInt(binding.progressBar,"progress",value).setDuration(1000).start()
            delay(1000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }



}