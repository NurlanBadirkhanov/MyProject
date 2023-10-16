package com.kirayeazerbaijan.kirayeaz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.kirayeazerbaijan.kirayeaz.DaggerHilt2.InitFirebaseViewModel
import com.kirayeazerbaijan.kirayeaz.R.*
import com.kirayeazerbaijan.kirayeaz.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var database: FirebaseDatabase
    val model: InitFirebaseViewModel by viewModels()

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        menuItem()
        model.initDatabase()
    }

    private fun menuItem() {
        binding.bottomAppBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                id.action_home -> {
                    navController.navigate(id.action_home)
                    true
                }
                id.action_category -> {
                    navController.navigate(id.action_category)
                    true
                }
                id.action_profile -> {

                    navController.navigate(id.registerFragment)
                    true
                }
                else -> {

                    navController.navigate(id.action_home)
                    true
                }
            }
        }
    }


}