package com.azerbaijankiraye.kirayeaz.Profile.NEW

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kaopiz.kprogresshud.KProgressHUD
import com.azerbaijankiraye.kirayeaz.AllAdapter
import com.azerbaijankiraye.kirayeaz.Profile.NewAds.CityAzerbaijan
import com.azerbaijankiraye.kirayeaz.Profile.NewAds.CustomSpinnerAdapter
import com.azerbaijankiraye.kirayeaz.databinding.FragmentNewAdsStudentsBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class NewAdsStudentsFragment : Fragment() {
    private lateinit var binding: FragmentNewAdsStudentsBinding
    private lateinit var adapter: AllAdapter
    private val PICK_IMAGE_REQUEST = 1
    private val MAX_IMAGES = 5
    private lateinit var auth: FirebaseAuth
    private var selectedNewOrOld: String = ""
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var imSelected: ImageView
    private lateinit var imSelected1: ImageView
    private lateinit var imSelected2: ImageView
    private lateinit var imSelected3: ImageView
    private lateinit var imSelected4: ImageView
    private var selectedGiveOreSearch: String = ""
    private val imageLinks = HashMap<String, String>()
    private var hud: KProgressHUD? = null
    private var originalStatusBarColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAdsStudentsBinding.inflate(layoutInflater, container, false)
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), com.azerbaijankiraye.kirayeaz.R.color.Student_Color)

        return binding.root
    }
    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bBack.setOnClickListener {
            findNavController().popBackStack()
        }
        initFirebase()
        cityNameArray()
        adapter = AllAdapter(requireContext())
        getDataBase()
        spinnersGiveOreSearch()
        initIm()
        buttonAddNewAds()
        spinnerOlnOreNew()
        getDataBase()
    }
    private fun getDataBase() {
        binding.apply {
            val child = database.reference.child("users").child(auth.uid.toString())
            child.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Проверяем, есть ли в базе данных поле "gmail" для текущего пользователя
                    if (snapshot.hasChild("gmail")) {
                        val gmailValue = snapshot.child("gmail").getValue(String::class.java)
                        val getName = snapshot.child("name").getValue(String::class.java)
                        val number =  snapshot.child("number").getValue(String::class.java)

                        binding.editGmail.setText(gmailValue)
                        binding.editName.setText(getName)
                        binding.editNumber.setText(number)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибки при чтении из базы данных
                }
            })
        }
    }
    private fun spinnersGiveOreSearch() {
        val spinnersOldNeW = binding.spinnerOldOrNew
        val itemOldNew = resources.getStringArray(com.azerbaijankiraye.kirayeaz.R.array.item_old_new)
        val adapter = CustomSpinnerAdapter(requireContext(), itemOldNew)
        spinnersOldNeW.adapter = adapter

        spinnersOldNeW.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGiveOreSearch = itemOldNew[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Добавьте логику, если ничего не выбрано
            }
        }
    }


    private fun spinnerOlnOreNew() {
        val spinner = binding.spinnerCateg
        val itemOldNew = resources.getStringArray(com.azerbaijankiraye.kirayeaz.R.array.new_home_types)
        val adapter = CustomSpinnerAdapter(requireContext(), itemOldNew)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedNewOrOld = itemOldNew[position]


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Добавьте логику, если ничего не выбрано
            }
        }
    }

    private fun buttonAddNewAds() {
        binding.imSetImage.setOnClickListener {
            if (selectedImages.size < MAX_IMAGES) {
                openImagePicker()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Максимум $MAX_IMAGES фотографий",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.bToAddNewAds.setOnClickListener {
            showProgresBar(requireContext())
            val title = binding.editTitle.text.toString()
            val price = binding.edPrice.text.toString()
            val city = binding.editCity.text.toString()
            val desc = binding.editDesc.text.toString()
            val name = binding.editName.text.toString()
            val number = binding.editNumber.text.toString()
            val gmail = binding.editGmail.text.toString()
            val square = binding.editSquare.text.toString()
            val roomQuantity = binding.editRoom.text.toString()

            if (title.isEmpty() || price.isEmpty() || city.isEmpty() || desc.isEmpty() ||
                name.isEmpty() || number.isEmpty() || gmail.isEmpty() || square.isEmpty()
            ) {
                hud?.dismiss()
                Toast.makeText(requireContext(), getString(com.azerbaijankiraye.kirayeaz.R.string.new_toast), Toast.LENGTH_LONG).show()
            } else {

                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("users")

                val uid = auth.uid

                val balanceReference = reference.child(uid.toString()).child("balance")

                // Прочитайте текущее значение баланса
                balanceReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentBalance = snapshot.getValue(Int::class.java) ?: 0

                        if (currentBalance > 0) {
                            // Если баланс больше 0, выполните вычитание 1
                            val newBalance = currentBalance - 1

                            // Обновите значение баланса в базе данных
                            balanceReference.setValue(newBalance).addOnSuccessListener {

                                if (selectedImages.isNotEmpty()) {
                                    uploadImagesToFirebaseStorage(
                                        title,
                                        price,
                                        city,
                                        desc,
                                        selectedNewOrOld,
                                        selectedGiveOreSearch,
                                        name,
                                        number,
                                        gmail,
                                        square,
                                        roomQuantity
                                    )


                                } else {
                                    hud!!.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        "Выберите хотя бы одно изображение",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }.addOnFailureListener {
                                hud!!.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "Произошла ошибка при обновлении баланса.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            hud!!.dismiss()
                            Toast.makeText(
                                requireContext(),
                                "Недостаточно баланса для публикации объявления.",
                                Toast.LENGTH_SHORT
                            ).show()
//                            val bottomSheetFragment = AddBalanceFragment()
//                            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        hud!!.dismiss()
                        Toast.makeText(
                            requireContext(),
                            "Ошибка при проверке баланса.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val clipData = data.clipData
            clipData?.let {
                for (i in 0 until it.itemCount) {
                    if (selectedImages.size < MAX_IMAGES) {
                        val imageUri = it.getItemAt(i).uri
                        selectedImages.add(imageUri)
                    } else {
                        break
                    }
                }
                displaySelectedImages()
            } ?: run {
                val imageUri = data.data
                if (imageUri != null && selectedImages.size < MAX_IMAGES) {
                    selectedImages.add(imageUri)
                    displaySelectedImages()
                }
            }
        }
    }

    private fun displaySelectedImages() {
        val imageViews = arrayOf(imSelected, imSelected1, imSelected2, imSelected3, imSelected4)
        val requestOptions = RequestOptions()
            .override(512, 512) // Максимальные размеры изображения (1 МБ)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        for (i in 0 until selectedImages.size) {
            val imageView = imageViews[i]
            Glide.with(requireContext())
                .load(selectedImages[i])
                .apply(requestOptions)
                .into(imageView)
            imageView.visibility = View.VISIBLE
        }
    }

    private fun cityNameArray() {
        val cityAzerbaijan = CityAzerbaijan()
        val cities = cityAzerbaijan.city()

        val autoCompleteCity = binding.editCity
        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, cities)
        autoCompleteCity.setAdapter(adapter)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun showProgresBar(context: Context) {
        hud = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(getString(com.azerbaijankiraye.kirayeaz.R.string.new_load))
            .setCancellable(true)
            .setAnimationSpeed(2)

        hud?.show()
    }

    private fun initIm() {
        imSelected = binding.im1
        imSelected1 = binding.im2
        imSelected2 = binding.im3
        imSelected3 = binding.im4
        imSelected4 = binding.im5
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }

    private fun saveImageLinks(imageUrls: List<String>) {
        for (imageUrl in imageUrls) {
            val imageId = generateUniqueId(imageUrl)

            imageLinks[imageId] = imageUrl
        }
    }

    private fun generateUniqueId(imageUrl: String): String {
        return imageUrl.hashCode().toString()
    }

    private fun uploadImagesToFirebaseStorage(
        title: String,
        price: String,
        city: String,
        desc: String,
        selectedCategory: String,
        selectedGiveOreSearch: String,
        name: String,
        number: String,
        gmail: String,
        square: String,
        roomQuantity: String,
    ) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {

            database.reference.child("users").child(uid)

            val imageUrls = mutableListOf<String>()
            val imageCount = selectedImages.size

            for (imageUri in selectedImages) {
                val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                if (inputStream != null) {
                    val compressedInputStream =
                        compressImage(inputStream, 300) // Ограничение до 300 кБ

                    imageRef.putStream(compressedInputStream)
                        .addOnSuccessListener { taskSnapshot ->
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                imageUrls.add(imageUrl)

                                if (imageUrls.size == imageCount) {
                                    saveImageLinks(imageUrls)
                                    saveToDatabase(
                                        title,
                                        price,
                                        city,
                                        desc,
                                        selectedCategory,
                                        selectedGiveOreSearch,
                                        name,
                                        number,
                                        gmail,
                                        square,
                                        roomQuantity,
                                    )
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Error uploading image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

    private fun compressImage(inputStream: InputStream, targetSizeKb: Int): InputStream {
        val outputStream = ByteArrayOutputStream()
        val targetSize = targetSizeKb * 1024 // Конвертировать в байты

        val bitmap = BitmapFactory.decodeStream(inputStream)
        var quality = 80 // Начальное значение качества JPEG

        var bytes: ByteArray
        do {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            bytes = outputStream.toByteArray()
            quality -= 10 // Уменьшьте качество на 10 единиц при каждой итерации.
        } while (bytes.size > targetSize && quality >= 0)

        inputStream.close()
        return ByteArrayInputStream(bytes)
    }


    private fun saveToDatabase(
        title: String,
        price: String,
        city: String,
        desc: String,
        selectedNewOrOld: String,
        selectedGiveOreSearch: String,
        name: String,
        number: String,
        gmail: String,
        square: String,
        roomQuantity: String
    ) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)
            val adsRef = userRef.child("ads")
            val todayData = ServerValue.TIMESTAMP
            val category = "Для Студентов"

            val data = mapOf(
                "title" to title,
                "price" to price,
                "city" to city,
                "desc" to desc,
                "category" to category,
                "selectedHome" to selectedNewOrOld,
                "giveOreSearch" to selectedGiveOreSearch,
                "name" to name,
                "number" to number,
                "gmail" to gmail,
                "imageUrls" to imageLinks,
                "square" to square,
                "roomQuantity" to roomQuantity,
                "date" to todayData,
            )

            val newAdRef = adsRef.push()

            newAdRef.setValue(data)
                .addOnSuccessListener {
                    hud!!.dismiss()
                    binding.apply {
                        editTitle.text.clear()
                        edPrice.text.clear()
                        editCity.text.clear()
                        editDesc.text.clear()
                        editRoom.text.clear()
                        editSquare.text.clear()
                        editName.text.clear()
                        editGmail.text.clear()
                        editNumber.text.clear()
                        findNavController().navigate(com.azerbaijankiraye.kirayeaz.R.id.action_newAdsStudentsFragment_to_okFragment)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка сохранения данных",
                        Toast.LENGTH_SHORT
                    ).show()
                    hud!!.dismiss()
                    binding.progressBar.visibility = View.GONE
                    Log.d("MyLog", "${it.message}")
                }

            val productsRef = database.reference.child("adminStudents").push()

            val productWithId = data + ("id" to productsRef.key)
            productsRef.setValue(productWithId)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Данные Отправлены на Проверку !",
                        Toast.LENGTH_LONG

                    ).show()
                    binding.apply {
                        editTitle.text.clear()
                        edPrice.text.clear()
                        editCity.text.clear()
                        editDesc.text.clear()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка сохранения данных",
                        Toast.LENGTH_LONG

                    ).show()
                    binding.progressBar.visibility = View.GONE
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Пользователь не аутентифицирован!",
                Toast.LENGTH_LONG
            ).show()
            binding.progressBar.visibility = View.GONE
        }
    }





}