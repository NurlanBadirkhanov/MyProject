package com.azerbaijankiraye.kirayeaz.Profile.NewAds

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class CustomSpinnerAdapter(context: Context, private val items: Array<String>) : ArrayAdapter<String>(context, com.azerbaijankiraye.kirayeaz.R.layout.my_spinners_item, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        // Здесь вы можете настроить внешний вид текста в Spinner
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        // Здесь вы можете настроить внешний вид выпадающего списка
        return view
    }
}