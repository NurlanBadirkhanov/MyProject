package com.creatives.vakansiyaaz.Login.Number

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creatives.vakansiyaaz.databinding.ActivityNumberBinding

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
//    private lateinit var telegramBot: TelegramVerification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)



}
}





