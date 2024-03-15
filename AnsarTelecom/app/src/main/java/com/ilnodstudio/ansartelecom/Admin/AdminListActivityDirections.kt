package com.ilnodstudio.ansartelecom.Admin

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.ActivityAdminListDirectionsBinding
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class AdminListActivityDirections : AppCompatActivity() {
    lateinit var binding: ActivityAdminListDirectionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListDirectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        b()
        val getProduct = intent.getSerializableExtra("data") as? AdminData
        if (getProduct != null) {
            val originalTitle = getProduct.Price.toString()
            val additionalWord = " AZN"
            binding.tvPriceDetail.text =  originalTitle + additionalWord
            binding.tvTitle.text = getProduct.title
            binding.rvDesc.text = getProduct.description

            val imageUrls = getProduct.imgUrlList

            setupCarouselView(imageUrls)

        }

    }
    private fun b() {
        binding.backAdmin.setOnClickListener {
          onBackPressed()

        }
        binding.bDelete.setOnClickListener {
            val getProduct = intent.getSerializableExtra("data") as? AdminData

            if (getProduct != null) {
                deleteRecordByItemKey(
                    getProduct.itemKey
                )
            }
            if (getProduct != null) {
                deleteRecordByItemKeyFromModels(
                    getProduct.itemKey
                )
            }
            if (getProduct != null) {
                val itemKey = getProduct.itemKey
                deleteRecordByItemKey(itemKey)
                deleteImagesFromStorage(itemKey)
            }
        }
    }

    private fun deleteRecordByItemKey(itemKey: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("products")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnapshot in snapshot.children) {
                    for (mainCategorySnapshot in categorySnapshot.children) {
                        for (subCategorySnapshot in mainCategorySnapshot.children) {
                            for (modelSnapshot in subCategorySnapshot.children) {
                                val itemSnapshot = modelSnapshot.child(itemKey)
                                if (itemSnapshot.exists()) {
                                    val mTask = itemSnapshot.ref.removeValue()

                                    mTask.addOnSuccessListener {
                                        Toast.makeText(this@AdminListActivityDirections, "Запись удалена", Toast.LENGTH_LONG).show()
                                       finish()
                                    }.addOnFailureListener { error ->
                                        Toast.makeText(this@AdminListActivityDirections, "Ошибка при удалении: ${error.message}", Toast.LENGTH_LONG).show()
                                    }
                                    return
                                }
                            }
                        }
                    }
                }

                // Если ключ не найден, вы можете вывести сообщение об ошибке
                Toast.makeText(this@AdminListActivityDirections, "Элемент с ключом $itemKey не найден", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminListActivityDirections, "Ошибка: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun deleteRecordByItemKeyFromModels(itemKey: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("products")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnapshot in snapshot.children) {
                    for (mainCategorySnapshot in categorySnapshot.children) {
                        for (subCategorySnapshot in mainCategorySnapshot.children) {
                            if (subCategorySnapshot.key != "Мобильные Телефоны") {
                                for (itemSnapshot in subCategorySnapshot.children) {
                                    if (itemSnapshot.key == itemKey) {
                                        val mTask = itemSnapshot.ref.removeValue()

                                        mTask.addOnSuccessListener {
                                            Toast.makeText(this@AdminListActivityDirections, "Запись удалена", Toast.LENGTH_LONG).show()
                                            finish()
                                        }.addOnFailureListener { error ->
                                            Toast.makeText(this@AdminListActivityDirections, "Ошибка при удалении: ${error.message}", Toast.LENGTH_LONG).show()
                                        }
                                        return
                                    }
                                }
                            } else {
                                for (modelSnapshot in subCategorySnapshot.children) {
                                    val itemSnapshot = modelSnapshot.child(itemKey)
                                    if (itemSnapshot.exists()) {
                                        val mTask = itemSnapshot.ref.removeValue()

                                        mTask.addOnSuccessListener {
                                            Toast.makeText(this@AdminListActivityDirections, "Запись удалена", Toast.LENGTH_LONG).show()
                                            // Ваш код, который нужно выполнить после успешного удаления
                                        }.addOnFailureListener { error ->
                                            Toast.makeText(this@AdminListActivityDirections, "Ошибка при удалении: ${error.message}", Toast.LENGTH_LONG).show()
                                        }
                                        return
                                    }
                                }
                            }
                        }
                    }
                }

                // Если ключ не найден, вы можете вывести сообщение об ошибке
                Toast.makeText(this@AdminListActivityDirections, "Элемент с ключом $itemKey не найден", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminListActivityDirections, "Ошибка: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun deleteImagesFromStorage(itemKey: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("product_images").child(itemKey)

        imagesRef.listAll()
            .addOnSuccessListener { listResult ->
                val deletePromises = mutableListOf<Task<Void>>()

                for (imageRef in listResult.items) {
                    val deletePromise = imageRef.delete()
                    deletePromises.add(deletePromise)
                }

                Tasks.whenAll(deletePromises)
                    .addOnSuccessListener {
                        // Все изображения успешно удалены
                    }
                    .addOnFailureListener { exception ->
                        // Обработка ошибок при удалении изображений
                    }
            }
            .addOnFailureListener { exception ->
                // Обработка ошибок при получении списка изображений
            }
    }
 private fun setupCarouselView(imageUrls: List<String?>) {
        val carouselView: CarouselView = findViewById(R.id.carouselView)
        carouselView.setImageListener(object : ImageListener {
            override fun setImageForPosition(position: Int, imageView: ImageView?) {
                imageView?.let {
                    Glide.with(this@AdminListActivityDirections)
                        .load(imageUrls[position])
                        .apply(RequestOptions().override(512, 512))
                        .into(it)
                }
            }
        })
        carouselView.pageCount = imageUrls.size
    }
}