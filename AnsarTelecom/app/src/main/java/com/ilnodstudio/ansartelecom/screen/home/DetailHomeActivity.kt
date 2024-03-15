package com.ilnodstudio.ansartelecom.screen.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.ActivityDetailHomeBinding
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class DetailHomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDetail()
    }

    private fun getDetail() {
        binding.apply {
            backHome.setOnClickListener {
                finish()
            }

            val getProduct = intent.getSerializableExtra("data") as? HomeModel
            if (getProduct != null) {
                val originalTitle = getProduct.Price.toString()
                val additionalWord = " AZN"
                binding.tvPriceDetail.text =  originalTitle + additionalWord
                binding.tvTitle.text = getProduct.title
                binding.rvDesc.text = getProduct.description

                val imageUrls = getProduct.imgUrlList

                setupCarouselView(imageUrls)

            }
        }
    }
    private fun setupCarouselView(imageUrls: List<String?>) {
        val carouselView: CarouselView = findViewById(R.id.carouselView)
        carouselView.setImageListener(object : ImageListener {
            override fun setImageForPosition(position: Int, imageView: ImageView?) {
                imageView?.let {
                    Glide.with(this@DetailHomeActivity)
                        .load(imageUrls[position])
                        .apply(RequestOptions().centerCrop())
                        .into(it)

                    it.setOnClickListener {
                        // Handle image click here
                        openFullScreenImage(imageUrls, position)
                    }
                }
            }
        })
        carouselView.pageCount = imageUrls.size
    }



    private fun openFullScreenImage(imageUrls: List<String?>, position: Int) {
        val intent = Intent(this, FullScreenImageActivity::class.java)
        intent.putStringArrayListExtra("image_urls", ArrayList(imageUrls))
        intent.putExtra("position", position)
        startActivity(intent)
    }








}