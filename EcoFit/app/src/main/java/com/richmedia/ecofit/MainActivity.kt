package com.richmedia.ecofit

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import com.richmedia.ecofit.Adapter.EcoAdapter
import com.richmedia.ecofit.Adapter.EcoData
import com.richmedia.ecofit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EcoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rcInit()

    }

    private fun rcInit() {
        adapter = EcoAdapter(this)
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        fakeData()
    }
    private fun fakeData(){
        val list:MutableList<EcoData> = mutableListOf()

        val dataArray = arrayOf( data1,
            data, data3, data4, data5, data6, data7, data8, data9, data10,
                    data11, data13, data15, data16, data17, data18, data19, data12, data14

        )

        dataArray.forEach { data ->
            list.add(data)
        }

        adapter.setData(list)




    }


}