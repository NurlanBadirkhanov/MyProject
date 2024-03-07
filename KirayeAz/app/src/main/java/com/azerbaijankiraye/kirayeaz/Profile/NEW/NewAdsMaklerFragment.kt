package com.azerbaijankiraye.kirayeaz.Profile.NEW

import com.azerbaijankiraye.kirayeaz.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
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
import com.azerbaijankiraye.kirayeaz.databinding.FragmentNewAdsMaklerBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class NewAdsMaklerFragment : Fragment() {
    private lateinit var binding: FragmentNewAdsMaklerBinding
    private var originalStatusBarColor: Int = 0
    private lateinit var adapter: AllAdapter
    private val PICK_IMAGE_REQUEST = 1
    private val MAX_IMAGES = 5
    private lateinit var auth: FirebaseAuth
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var imSelected: ImageView
    private lateinit var imSelected1: ImageView
    private lateinit var imSelected2: ImageView
    private lateinit var imSelected3: ImageView
    private lateinit var imSelected4: ImageView
    private var spinner1: String = ""
    private var spinner2: String = ""
    private var spinner3: String = ""
    private val imageLinks = HashMap<String, String>()
    private var hud: KProgressHUD? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAdsMaklerBinding.inflate(layoutInflater, container, false)
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = getColor(requireContext(), R.color.Makler_Color)
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
        initIm()
        buttonAddNewAds()
        setupSpinners()
        getDataBase()
    }

    private fun getDataBase() {
        binding.apply {
            val child = database.reference.child("users").child(auth.uid.toString())
            child.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Проверяем, есть ли в базе данных поле "gmail" для текущего пользователя
                        val gmailValue = snapshot.child("gmail").getValue(String::class.java)
                        val getName = snapshot.child("name").getValue(String::class.java)
                        val getNumber = snapshot.child("number").getValue(String::class.java)


                        binding.editGmail.setText(gmailValue)
                        binding.editName.setText(getName)
                        binding.editNumber.setText(getNumber)

                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибки при чтении из базы данных
                }
            })
        }
    }

    private fun setupSpinners() {
        // Настройка первого спиннера (аренда/продажа)
        val spinnerOldOrNew = binding.spinner1
        val itemOldNew = arrayOf(
            getString(R.string.maklers_send_farg),
            getString(R.string.maklers_arenda_frag)
        )
        val adapterOldNew = CustomSpinnerAdapter(requireContext(), itemOldNew)
        spinnerOldOrNew.adapter = adapterOldNew

        spinnerOldOrNew.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinner1 = itemOldNew[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Добавьте логику, если ничего не выбрано
            }
        }

        // Настройка второго спиннера (объекты: квартиры, дома, дачи, участки)
        val spinnerCateg = binding.spinner2
        val itemCateg = resources.getStringArray(R.array.itemCateg)
        val adapterCateg = CustomSpinnerAdapter(requireContext(), itemCateg)
        spinnerCateg.adapter = adapterCateg

        spinnerCateg.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinner2 = itemCateg[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Добавьте логику, если ничего не выбрано
            }
        }


        // Настройка третьего спиннера (новостройка/вторичное жилье)
        val spinnerType = binding.spinner3
        val itemType = arrayOf(getString(R.string.new_newHome), getString(R.string.new_oldHome))
        val adapterType = CustomSpinnerAdapter(requireContext(), itemType)
        spinnerType.adapter = adapterType

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinner3 = itemType[position]
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
                    "Max $MAX_IMAGES Photo",
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
            val nameOrg = binding.editNameOrg.text.toString()

            if (title.isEmpty() || price.isEmpty() || city.isEmpty() || desc.isEmpty() ||
                name.isEmpty() || number.isEmpty() || gmail.isEmpty() || square.isEmpty()
            ) {
                hud?.dismiss()
                Toast.makeText(requireContext(), getString(R.string.new_toast), Toast.LENGTH_LONG)
                    .show()
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
                                        spinner1,
                                        spinner2,
                                        spinner3,
                                        name,
                                        number,
                                        gmail,
                                        square,
                                        roomQuantity,
                                        nameOrg,
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
                                    getString(R.string.new_error_balance_toast),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            hud!!.dismiss()
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.new_balance),
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

    @Deprecated("Deprecated in Java")
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
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
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
            .setLabel(getString(R.string.new_load))
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
        spinner1: String,
        spinner2: String,
        spinner3: String,
        name: String,
        number: String,
        gmail: String,
        square: String,
        roomQuantity: String,
        nameOrg: String,
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
                                        spinner1,
                                        spinner2,
                                        spinner3,
                                        name,
                                        number,
                                        gmail,
                                        square,
                                        roomQuantity,
                                        nameOrg
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
        spinner1: String,
        spinner2: String,
        spinner3: String,
        name: String,
        number: String,
        gmail: String,
        square: String,
        roomQuantity: String,
        nameOrg: String,
    ) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)
            val adsRef = userRef.child("ads")
            val todayData = ServerValue.TIMESTAMP

            val data = mapOf(
                "title" to title,
                "price" to price,
                "city" to city,
                "desc" to desc,
                "category" to spinner1,
                "subCategory" to spinner2,
                "selectedHome" to spinner3,
                "name" to name,
                "number" to number,
                "gmail" to gmail,
                "imageUrls" to imageLinks,
                "square" to square,
                "roomQuantity" to roomQuantity,
                "date" to todayData,
                "nameOrg" to nameOrg,

                )

            val newAdRef = adsRef.push()
            newAdRef.setValue(data)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.action_newAdsMaklerFragment_to_okFragment)
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
                }

            val adminRef = database.reference.child("adminMaklers").push()

            val productWithId = data + ("id" to adminRef.key)
            adminRef.setValue(productWithId)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Данные успешно сохранены",
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
                        "Ошибка сохранения данных ",
                        Toast.LENGTH_LONG

                    ).show()
                    binding.progressBar.visibility = View.GONE
                    Log.d("MyLog", "${it.message}")
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