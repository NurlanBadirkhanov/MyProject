package com.azerbaijankiraye.kirayeaz.Home

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var imageUrls: List<String?>
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Скрыть строку навигации (navigation bar) - опционально
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // Сделать активити полноэкранным
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        imageUrls = intent.getStringArrayListExtra("image_urls") ?: emptyList()
        currentPosition = intent.getIntExtra("position", 0)

        val viewPager = binding.viewPager
        val adapter = FullScreenImageAdapter(imageUrls,this)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(currentPosition, false)
    }
}
