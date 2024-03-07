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
import com.azerbaijankiraye.kirayeaz.Home.FullscreenActivity
import com.azerbaijankiraye.kirayeaz.Home.HomeDetailActivity
import com.azerbaijankiraye.kirayeaz.PhotoPagerAdapter
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.UsersDataMy
import com.azerbaijankiraye.kirayeaz.databinding.ActivityAdminDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.synnapps.carouselview.CarouselView
import java.time.Instant
import java.time.ZoneId

class StudentsDetailActivity : AppCompatActivity() {

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
        getProductS()
        initFirebase()
        buttons()
        visibilityElements()

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
                Glide.with(this@StudentsDetailActivity)
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

    private fun buttons() {
        binding.apply {
            acceptButtonStudents.setOnClickListener {
                val giveOreSearch = getProduct.giveOreSearch
                moveAdminDataStudents(giveOreSearch,getProduct.id)
            }

            deleteButtonStudents.setOnClickListener {
                deleteAdsStudents(getProduct.id)
            }
        }

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        acceptedAdsRef = database.reference.child("products").child("Для студентов")
    }
    private fun visibilityElements() {
        binding.acceptButtonMaklers.visibility = View.GONE
        binding.deleteButtonMaklers.visibility = View.GONE
        binding.acceptButtonSimple.visibility = View.GONE
        binding.deleteButtonSimple.visibility = View.GONE
    }

    private fun moveAdminDataStudents(giveOreSearch: String,itemId: String) {
        val adminRef = database.reference.child("adminStudents")

        adminRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                getProduct.giveOreSearch = giveOreSearch
                val ref = database.reference.child("adminStudents")


                // Создайте уникальный ключ для нового объекта в узле "products"
                val productKey = database.reference.child("products").child("Для студентов")
                    .child(giveOreSearch)
                    .child(itemId).key

                val productRef = database.reference.child("products").child("Для студентов")
                    .child(giveOreSearch)
                    .child(productKey!!)

                productRef.setValue(getProduct)
                    .addOnSuccessListener {
                        // Удаляем данные из "adminStudents"
                        ref.removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@StudentsDetailActivity,
                                    "Данные успешно перенесены из adminStudents в products и удалены из adminSimple",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@StudentsDetailActivity,
                                    "Ошибка при удалении данных из adminStudents: ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@StudentsDetailActivity,
                            "Ошибка при копировании данных: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@StudentsDetailActivity,
                    "Ошибка получения данных из adminStudents: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun deleteAdsStudents(itemId: String) {
        val adminRef = database.reference.child("adminStudents").child(itemId)

        adminRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(
                    this@StudentsDetailActivity,
                    "Объявление успешно удалено!",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@StudentsDetailActivity,
                    "Ошибка удаления объявления: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}