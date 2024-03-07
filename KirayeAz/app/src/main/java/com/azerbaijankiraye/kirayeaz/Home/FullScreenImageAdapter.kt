package com.azerbaijankiraye.kirayeaz.Home

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.azerbaijankiraye.kirayeaz.R
import com.bumptech.glide.Glide

class FullScreenImageAdapter(private val imageUrls: List<String?>,private val activity: Activity) : RecyclerView.Adapter<FullScreenImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fullscreen_image, parent, false)
        return ImageViewHolder(view)
    }

    private var activityContext: Context? = null

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            // Здесь используем activity для завершения
            activity.finish()
        }

    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fullScreenImageView)
    }
}
