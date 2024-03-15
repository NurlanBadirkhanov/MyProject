package com.ilnodstudio.ansartelecom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ilnodstudio.ansartelecom.Admin.AdminActivity
import com.ilnodstudio.ansartelecom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationView()
        navigationDrawer()
        MAIN = this
        binding.openMenu.setOnClickListener {
            if (binding.Drawer.isDrawerOpen(GravityCompat.START)) {
                binding.Drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.Drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun navigationDrawer() {
        binding.nv.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_admin-> {
                    val intent = Intent(this@MainActivity, AdminActivity::class.java)
                    startActivity(intent)
                    binding.Drawer.closeDrawer(GravityCompat.START)

                }
            }
            true
        }

        }

    private fun setupBottomNavigationView() {
        navController = findNavController(R.id.nav_host_fragment)
        binding.chipNavigationBar.setItemSelected(id = R.id.home,true)

        binding.chipNavigationBar.setOnItemSelectedListener {
            when (it) {
                R.id.home -> {
                    navController.navigate(R.id.home)
                    binding.openMenu.visibility = View.VISIBLE

                }
                R.id.list -> {
                    navController.navigate(R.id.list)
                    binding.openMenu.visibility = View.VISIBLE

                }
                R.id.info -> {
                    navController.navigate(R.id.info)
                    binding.openMenu.visibility = View.GONE

                }

                else -> {
                    navController.navigate(R.id.home)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
