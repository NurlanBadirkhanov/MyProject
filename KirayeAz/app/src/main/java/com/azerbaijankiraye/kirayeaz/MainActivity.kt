package com.azerbaijankiraye.kirayeaz

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kaopiz.kprogresshud.KProgressHUD
import com.azerbaijankiraye.kirayeaz.R.id
import com.azerbaijankiraye.kirayeaz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val languageManager by lazy { LanguageManager(this) }


    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var hud: KProgressHUD? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val currentLanguage = languageManager.getSavedLanguage()

        if (currentLanguage.isEmpty()) {
            languageManager.saveLanguage("az")
            languageManager.setLocale()
            recreate()
        } else {
            // В противном случае установить сохраненный язык
            languageManager.setLocale()

        }
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        menuItem()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Сохранить текущий язык при уничтожении активности
        languageManager.saveLanguage(languageManager.getSavedLanguage())
    }



    private fun menuItem() {
        binding.bottomAppBar.selectedItemId = id.action_home
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
                    navController.navigate(id.action_profile)
                    true
                }
                id.action_cart ->{
                    navController.navigate(id.action_cart)
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