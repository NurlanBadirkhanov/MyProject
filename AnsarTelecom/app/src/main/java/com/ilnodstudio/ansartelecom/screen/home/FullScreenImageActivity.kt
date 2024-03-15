package com.ilnodstudio.ansartelecom.screen.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ilnodstudio.ansartelecom.R

class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var imageUrls: List<String?>
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        imageUrls = intent.getStringArrayListExtra("image_urls") ?: emptyList()
        currentPosition = intent.getIntExtra("position", 0)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = FullScreenImageAdapter(imageUrls)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(currentPosition, false)



    }
}




