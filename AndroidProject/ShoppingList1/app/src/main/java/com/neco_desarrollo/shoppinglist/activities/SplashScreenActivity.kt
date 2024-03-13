package com.neco_desarrollo.shoppinglist.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neco_desarrollo.shoppinglist.R
import com.neco_desarrollo.shoppinglist.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

lateinit var binding:ActivitySplashScreenBinding
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.Main).launch {

            binding.progressSplash.max=1000
            val value = 1000
            ObjectAnimator.ofInt(binding.progressSplash,"progress",value).setDuration(4000).start()
            delay(4000)
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))

            setSupportActionBar(null)
            setContentView(R.layout.activity_splash_screen)


        }

    }


}