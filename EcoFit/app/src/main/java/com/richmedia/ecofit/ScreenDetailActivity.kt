package com.richmedia.ecofit

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.richmedia.ecofit.Adapter.EcoData
import com.richmedia.ecofit.databinding.ActivityScreenDetailBinding

class ScreenDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityScreenDetailBinding
    private lateinit var getProduct :EcoData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScreenDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "О Рецепте"
        getProducts()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    private fun getProducts() {
        getProduct = (intent.getSerializableExtra("data") as? EcoData)!!
        binding.tvTitle.text = getProduct.title
        binding.textView3.text = getProduct.desc
        binding.imageView3.setImageResource(getProduct.img)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Для простоты просто вызываем метод onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}