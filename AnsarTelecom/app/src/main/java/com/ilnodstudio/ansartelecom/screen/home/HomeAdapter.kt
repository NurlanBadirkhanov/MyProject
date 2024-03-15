package com.ilnodstudio.ansartelecom.Admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.MobitemBinding
import com.ilnodstudio.ansartelecom.screen.home.HomeModel

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {

    class HomeHolder(val binding: MobitemBinding): RecyclerView.ViewHolder(binding.root)

    var productList: List<HomeModel> = emptyList()




    var onItemClick: ((HomeModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val binding = MobitemBinding.inflate(LayoutInflater.from(context), parent, false)
        return HomeHolder(binding)
    }

    fun setData(data: List<HomeModel>) {
        productList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        val data = productList[position]

        holder.binding.mobPrice.text = "${data.Price} AZN"
        holder.binding.mobName.text = data.title

        if (data.imgUrlList.isNotEmpty()) {
            val firstImageUrl = data.imgUrlList[0] // Берем первый URL изображения из списка
            Glide.with(context)
                .load(firstImageUrl)
                .apply(RequestOptions().override(512, 512))
                .into(holder.binding.imageView)
        } else {
            // Если список URL-ов пустой, можно загрузить заглушку или пустое изображение
            Glide.with(context)
                .load(R.drawable.ansar) // Заглушка или пустое изображение
                .apply(RequestOptions().override(512, 512))
                .into(holder.binding.imageView)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data)
        }
    }



}
