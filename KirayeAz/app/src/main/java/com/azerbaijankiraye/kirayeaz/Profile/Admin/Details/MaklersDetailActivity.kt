package com.azerbaijankiraye.kirayeaz.Profile.Admin.Details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.azerbaijankiraye.kirayeaz.*
import com.azerbaijankiraye.kirayeaz.Home.FullscreenActivity
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.ActivityAdminDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.synnapps.carouselview.CarouselView
import java.time.Instant
import java.time.ZoneId

class MaklersDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var acceptedAdsRef: DatabaseReference
    lateinit var getProduct: UsersDataMy



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFirebase()
        buttons()
        visibilityElements()
        getProductS()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getProductS() {
        getProduct = (intent.getSerializableExtra("data") as? UsersDataMy)!!
        val originalTitle = getProduct.price
        val additionalWord = " AZN"
        val imageUrls = getProduct.imageUrls



        binding.apply {
            tvPriceDetail.text = originalTitle + additionalWord
            tvTitle.text = getProduct.title
            rvDesc.text = getProduct.desc
            name.text = getProduct.name

            Novostroyka.text = getProduct.selectedHome
            Tvnumber.text = getProduct.number
            tvCityHome.text = getProduct.city
            tvSubCategory.text = getProduct.subCategory
            gmail.text = getProduct.gmail
            tvCategory.text = getProduct.category
            tvRoom.text = getProduct.roomQuantity
            tvSquare.text = getProduct.square
            val milliseconds = getProduct.date
            val date = Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate()
            Data.text = date.toString()
            setupCarouselView(imageUrls)

        }
    }
    private fun setupCarouselView(imageUrls: List<String?>) {
        val carouselView: CarouselView = findViewById(R.id.carouselView)
        carouselView.setImageListener { position, imageView ->
            imageView?.let {
                Glide.with(this@MaklersDetailActivity)
                    .load(imageUrls[position])
                    .apply(RequestOptions().centerCrop())
                    .into(it)

                it.setOnClickListener {
                    // Handle image click here
                    openFullScreenImage(imageUrls, position)
                }
            }
        }
        carouselView.pageCount = imageUrls.size
    }



    private fun openFullScreenImage(imageUrls: List<String?>, position: Int) {
        val intent = Intent(this, FullscreenActivity::class.java)
        intent.putStringArrayListExtra("image_urls", ArrayList(imageUrls))
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun visibilityElements() {
        binding.acceptButtonStudents.visibility = View.GONE
        binding.deleteButtonStudents.visibility = View.GONE
        binding.acceptButtonSimple.visibility = View.GONE
        binding.deleteButtonSimple.visibility = View.GONE
    }

    private fun buttons() {
        binding.apply {
            acceptButtonMaklers.setOnClickListener {
                val category = getProduct.category
                val subCategory = getProduct.subCategory
                moveAdminDataMaklers(category,getProduct.id)
            }

            deleteButtonMaklers.setOnClickListener {
                deleteAdMaklers(getProduct.id)
            }
        }
    }


    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        acceptedAdsRef = database.reference.child("products").child("Для Маклеров")
    }

    private fun moveAdminDataMaklers(category: String,itemId:String) {
        val adminRef = database.reference.child("adminMaklers")

        adminRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                getProduct.category = category
                val ref = database.reference.child("adminMaklers")


                // Создайте уникальный ключ для нового объекта в узле "products"
                val productKey = database.reference.child("products").child("Для Маклеров")
                    .child(category)
                    .child(itemId).key

                val productRef = database.reference.child("products").child("Для Маклеров")
                    .child(category)
                    .child(productKey!!)

                productRef.setValue(getProduct)
                    .addOnSuccessListener {
                        // Удаляем данные из "adminStudents"
                        ref.removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@MaklersDetailActivity,
                                    "Данные успешно перенесены из adminStudents в products и удалены из adminSimple",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@MaklersDetailActivity,
                                    "Ошибка при удалении данных из adminStudents: ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@MaklersDetailActivity,
                            "Ошибка при копировании данных: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MaklersDetailActivity,
                    "Ошибка получения данных из adminStudents: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun deleteAdMaklers(itemId:String) {
        val adminRef = database.reference.child("adminMaklers").child(itemId)

        adminRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(
                    this@MaklersDetailActivity,
                    "Объявление успешно удалено!",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@MaklersDetailActivity,
                    "Ошибка удаления объявления: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}
