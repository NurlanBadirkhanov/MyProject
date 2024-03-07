package com.azerbaijankiraye.kirayeaz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.azerbaijankiraye.kirayeaz.databinding.AllItemBinding
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

////        WindowCompat.setDecorFitsSystemWindows(window, false)

class AllAdapter(val context: Context) : RecyclerView.Adapter<AllAdapter.AllHolder>(), Filterable {
    private var allList: MutableList<UsersDataMy> = mutableListOf()
    private var filteredList: List<UsersDataMy> = emptyList()

    var onItemClick: ((UsersDataMy)->Unit)? = null

    fun setData(data: List<UsersDataMy>) {
        allList.clear()
        allList.addAll(data)
        filteredList = data
        notifyDataSetChanged()
    }
    fun clear(data: List<UsersDataMy>) {
        allList.clear()
        notifyDataSetChanged()
    }


    fun removeItem(position: Int) {
        if (position >= 0 && position < allList.size) {
            allList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    fun sortDataByDescendingPrices() {
        filteredList = filteredList.sortedByDescending { it.price.toInt() }
        notifyDataSetChanged()
    }

    fun sortDataByAscendingPrices() {
        filteredList = filteredList.sortedBy { it.price.toInt() }
        notifyDataSetChanged()
    }

    fun sortDataByDate() {
        filteredList = filteredList.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllHolder {
        val binding = AllItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return AllHolder(binding)
    }

    override fun onBindViewHolder(holder: AllHolder, position: Int) {
        val setData = filteredList[position]
        setData.let {
            holder.binding.tvCity.text = it.city
            val price = it.price
            holder.binding.tvPrice.text = "$price AZN"
            holder.binding.tvTitle.text = it.title


            val formattedDate = convertMillisToDateString(it.date.toLong())
            holder.binding.TvData.text = formattedDate

            if (setData.imageUrls.isNotEmpty()) {
                val firstImageUrl = setData.imageUrls[0] // Берем первый URL изображения из списка
                Glide.with(context)
                    .load(firstImageUrl)
                    .apply(RequestOptions().override(512, 512))
                    .into(holder.binding.img)
            } else {
                // Если список URL-ов пустой, можно загрузить заглушку или пустое изображение
                Glide.with(context)
                    .load(R.drawable.kiraye) // Заглушка или пустое изображение
                    .apply(RequestOptions().override(512, 512))
                    .into(holder.binding.img)
            }
            }
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(setData)
            }
        }

    private fun convertMillisToDateString(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date(millis))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterPattern = constraint.toString().toLowerCase().trim()
                val results = FilterResults()

                if (filterPattern.isEmpty()) {
                    results.values = allList
                } else {
                    val filtered = mutableListOf<UsersDataMy>()
                    for (item in allList) {
                        if (item.title.toLowerCase().contains(filterPattern) ||
                            item.desc.toLowerCase().contains(filterPattern)
                        ) {
                            filtered.add(item)
                        }
                    }
                    results.values = filtered
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<UsersDataMy>
                notifyDataSetChanged()
            }
        }
    }

    class AllHolder(val binding: AllItemBinding) : RecyclerView.ViewHolder(binding.root)
}








