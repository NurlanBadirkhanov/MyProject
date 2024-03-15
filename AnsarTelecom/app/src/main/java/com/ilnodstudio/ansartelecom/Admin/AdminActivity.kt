package com.ilnodstudio.ansartelecom.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilnodstudio.ansartelecom.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }}


