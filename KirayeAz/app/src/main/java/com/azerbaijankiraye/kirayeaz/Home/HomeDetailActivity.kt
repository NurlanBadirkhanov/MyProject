package com.azerbaijankiraye.kirayeaz.Home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.UsersDataMy
import com.azerbaijankiraye.kirayeaz.databinding.ActivityHomeDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.synnapps.carouselview.CarouselView
import java.time.Instant
import java.time.ZoneId

class HomeDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeDetailBinding
    lateinit var getProduct: UsersDataMy
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProducts()
        initFirebase()
        buttons()
//        setupButtonClickListeners()



    }

    private fun buttons() {
        val delete = binding.bHeart
        val add = binding.bHeartNo
        val productIdToCheck = getProduct.id

        val database = FirebaseDatabase.getInstance()
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        // Установите слушатель кликов для кнопки удаления
        delete.setOnClickListener {
            // Удалить продукт из корзины
            add.visibility = View.VISIBLE
            delete.visibility = View.GONE
            deleteElementInBasket()
        }

        // Установите слушатель кликов для кнопки добавления
        add.setOnClickListener {
            // Добавить продукт в корзину
            add.visibility = View.GONE
            delete.visibility = View.VISIBLE
            setNewElementCortFirebase()
        }

        // Проверьте, есть ли продукт в корзине и обновите видимость кнопок
        basketRef.child(productIdToCheck).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Продукт уже в корзине
                    delete.visibility = View.VISIBLE
                    add.visibility = View.GONE
                } else {
                    // Продукта нет в корзине
                    add.visibility = View.VISIBLE
                    delete.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок при чтении данных
            }
        })

        binding.apply {
            imageButton.setOnClickListener {
                finish()
            }
            bCall.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:${getProduct.number}")
                startActivity(dialIntent)
            }



        }

    }
    private fun deleteElementInBasket() {
        val productIdToRemove = getProduct.id
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productIdToRemove).removeValue()

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    private fun setNewElementCortFirebase() {
        val productId = getProduct.id

        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productId).setValue(getProduct) // Можете использовать true как значение, или другое значение по вашему усмотрению
        buttons()
    }

    companion object {
        const val EXTRA_IMAGE_URLS = "extra_image_urls"
        const val EXTRA_POSITION = "extra_position"
    }


            @RequiresApi(Build.VERSION_CODES.O)
            private fun getProducts() {
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
                    tvnameOrg.text = getProduct.nameOrg
                    tvSubCategory.text = getProduct.subCategory

                    if (getProduct.subCategory.isNullOrEmpty()) {
                        tvSubCategory.text = getProduct.giveOreSearch
                    }
                    gmail.text = getProduct.gmail
                    tvCategory.text = getProduct.category
                    tvRoom.text = getProduct.roomQuantity
                    tvSquare.text = getProduct.square
                    val milliseconds = getProduct.date
                    val date =
                        Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    Data.text = date.toString()
                    setupCarouselView(imageUrls)
                }
            }
            private fun setupCarouselView(imageUrls: List<String?>) {
                val carouselView: CarouselView = findViewById(R.id.carouselView)
                carouselView.setImageListener { position, imageView ->
                    imageView?.let {
                        Glide.with(this@HomeDetailActivity)
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

        }

