package com.richmedia.ecofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        fakeData()

    }

    private fun rcInit() {
        adapter = EcoAdapter(this)
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        adapter.onItemClick = { adminData ->
            val intent = Intent(this, ScreenDetailActivity::class.java)
            intent.putExtra("data", adminData)
            startActivity(intent)
        }

    }

    private fun fakeData() {
        val list: MutableList<EcoData> = mutableListOf()
        val dataArray = arrayOf(
            data1,
            data, data3, data4, data5, data6, data7, data8, data9, data10,
            data11, data13, data15, data16, data9, data12, data14

        )

        dataArray.forEach { data ->
            list.add(data)
        }

        adapter.setData(list)


    }


}