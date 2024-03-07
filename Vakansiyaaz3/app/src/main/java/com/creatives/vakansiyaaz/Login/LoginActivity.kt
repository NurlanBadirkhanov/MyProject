package com.creatives.vakansiyaaz.Login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.creatives.vakansiyaaz.Login.Number.OtpModel
import com.creatives.vakansiyaaz.databinding.ActivityLogin2Binding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogin2Binding
    private lateinit var model: OtpModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[OtpModel::class.java]

    }
    override fun onResume() {
        super.onResume()
        model = ViewModelProvider(this)[OtpModel::class.java]
    }
}
