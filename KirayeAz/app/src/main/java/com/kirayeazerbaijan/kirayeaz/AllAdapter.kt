package com.kirayeazerbaijan.kirayeaz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirayeazerbaijan.kirayeaz.databinding.AllItemBinding

class AllAdapter(val context:Context):RecyclerView.Adapter<AllAdapter.AllHolder>() {
    class AllHolder(val binding:AllItemBinding):RecyclerView.ViewHolder(binding.root)

    var AllList: List<AllData> = emptyList()

    fun setData(data: List<AllData>) {
        AllList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllAdapter.AllHolder {
        val binding = AllItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return AllHolder(binding)
    }

    override fun onBindViewHolder(holder: AllAdapter.AllHolder, position: Int) {
        val setData = AllList[position]
        setData.let {
            holder.binding.tvCity.text = it.City
            val price = it.Price.toString()
            holder.binding.tvPrice.text = "$price AZN"
            holder.binding.tvTitle.text = it.Title
            it.Image?.let { it1 -> holder.binding.imName.setImageResource(it1) }



        }
    }

    override fun getItemCount(): Int {
       return AllList.size
    }

}
