package com.neco_desarrollo.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.neco_desarrollo.shoppinglist.R
import com.neco_desarrollo.shoppinglist.databinding.NewListDialogBinding

object NewListDialog {

   fun showDialog(context: Context, listener: Listener, name: String){
       var dialog: AlertDialog? = null
       val builder = AlertDialog.Builder(context)
       val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
       builder.setView(binding.root)
       binding.apply {
           edNewListName.setText(name)
           if(name.isNotEmpty()) bCreate.text = context.getString(R.string.update)
           bCreate.setOnClickListener {
                val listName = edNewListName.text.toString()
                if(listName.isNotEmpty()){
                    listener.onClick(listName)
                }
                dialog?.dismiss()
           }
       }
       dialog = builder.create()
       dialog.window?.setBackgroundDrawable(null)
       dialog.show()
   }
    interface Listener{
       fun onClick(name: String)
    }
}