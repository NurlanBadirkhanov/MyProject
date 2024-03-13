package com.richmedia.ecofit.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.richmedia.ecofit.R
import com.richmedia.ecofit.databinding.ItemEcoBinding

class EcoAdapter(val context: Context) : RecyclerView.Adapter<EcoAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemEcoBinding) : RecyclerView.ViewHolder(binding.root)

    private val list: MutableList<EcoData> = mutableListOf()

    var onItemClick: ((EcoData) -> Unit)? = null

    fun setData(data: List<EcoData>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEcoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let {
            Glide.with(holder.itemView.context)
                .load(it.img) // it.img - это URL изображения
                .placeholder(R.drawable.a) // placeholder - это ресурс-заглушка, отображаемая до загрузки изображения (необязательно)
                .error(android.R.drawable.stat_notify_error) // error - это ресурс-заглушка, отображаемая при ошибке загрузки изображения (необязательно)
                .transition(DrawableTransitionOptions.withCrossFade()) // Анимация смены изображений (необязательно)
                .into(holder.binding.imageView)

            holder.binding.tvTitle.text = it.title

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}