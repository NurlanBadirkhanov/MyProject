package com.azerbaijankiraye.kirayeaz.Profile.NEW

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.azerbaijankiraye.kirayeaz.databinding.FragmentNewAdsBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import com.azerbaijankiraye.kirayeaz.R

class NewAdsFragment : Fragment() {
    lateinit var binding: FragmentNewAdsBinding
    lateinit var adapter: AllAdapter
    private val PICK_IMAGE_REQUEST = 1
    private val MAX_IMAGES = 5
    private var originalStatusBarColor: Int = 0
    lateinit var auth: FirebaseAuth
    private var selectedCategory: String = ""
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var imSelected: ImageView
    private lateinit var imSelected1: ImageView
    private lateinit var imSelected2: ImageView
    private lateinit var imSelected3: ImageView
    private lateinit var imSelected4: ImageView
    private var selectedOldNewCategory: String = ""
    private var selectedSubCategory: String = ""

    private lateinit var selectedCategoryNodeRef: DatabaseReference // Ссылка на узел категории
    val imageLinks = HashMap<String, String>()
    var hud: KProgressHUD? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAdsBinding.inflate(inflater, container, false)
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.blue_bytf)
        context?.theme?.applyStyle(R.style.YourCustomTheme, true)

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
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        cityNameArray()
        adapter = AllAdapter(requireContext())
        spinnersOldNew()
        setupSpinners()
        initIm()
        getDataBase()

        binding.bBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.bToAddNewAds.setOnClickListener {
            ShowProgressBar(requireContext())

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
                Toast.makeText(
                    requireContext(),
                    getString(com.azerbaijankiraye.kirayeaz.R.string.new_toast),
                    Toast.LENGTH_LONG
                ).show()
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
                                        selectedCategory,
                                        selectedSubCategory,
                                        selectedOldNewCategory,
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
                                        getString(R.string.new_toast_photo),
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
                            getString(R.string.new_error_balance_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }


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
    }

    private fun initIm() {
        imSelected = binding.im1
        imSelected1 = binding.im2
        imSelected2 = binding.im3
        imSelected3 = binding.im4
        imSelected4 = binding.im5
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


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun ShowProgressBar(context: Context) {
        hud = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(getString(R.string.new_load))
            .setCancellable(true)
            .setAnimationSpeed(2)

        hud?.show()
    }


    private fun spinnersOldNew() {
        val spinnersOldNeW = binding.spinnerOldOrNew
        val itemOldNew = arrayOf(getString(R.string.new_newHome), getString(R.string.new_oldHome))
        val adapter = CustomSpinnerAdapter(requireContext(), itemOldNew)
        spinnersOldNeW.adapter = adapter

        spinnersOldNeW.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOldNewCategory = itemOldNew[position]


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Добавьте логику, если ничего не выбрано
            }
        }
    }

    private fun setupSpinners() {
        val spinnerCateg = binding.spinnerCateg
        val spinnerPodCategory = binding.spinnerPodCategory

        val itemsForFirstOption = arrayOf(
            getString(R.string.all_rent),
            getString(R.string.all_sales),
            getString(R.string.land_plots),
            getString(R.string.all_objects)
        )

        val itemsArenda = arrayOf(
            getString(R.string.rent_apartments),
            getString(R.string.rent_houses_cottages)
        )

        val itemsSend = arrayOf(
            getString(R.string.sales_apartments),
            getString(R.string.sales_houses_cottages)
        )

        val itemsWordSendBuy = arrayOf(
            getString(R.string.rent_land),
            getString(R.string.sales_land)
        )

        val itemsObject = arrayOf(
            getString(R.string.rent_objects),
            getString(R.string.sales_objects)
        )


        val adapterCateg = CustomSpinnerAdapter(requireContext(), itemsForFirstOption)
        val adapterPodCategory = CustomSpinnerAdapter(requireContext(), itemsForFirstOption)

        spinnerCateg.adapter = adapterCateg
        spinnerPodCategory.adapter = adapterPodCategory

        spinnerCateg.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = itemsForFirstOption[position]
                selectedCategoryNodeRef = database.reference.child("products")
                    .child(selectedCategory)

                when (position) {
                    0 -> {
                        binding.spinnerOldOrNew.visibility = View.VISIBLE
                        binding.editRoom.visibility = View.VISIBLE
                        binding.Rooms.visibility = View.VISIBLE

                        val adapter = CustomSpinnerAdapter(requireContext(), itemsArenda)
                        spinnerPodCategory.adapter = adapter
                        // Определите значения для подкатегорий этой категории
                        spinnerPodCategory.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedSubCategory = when (position) {
                                        0 -> "Аренда Квартир"
                                        1 -> "Аренда домов,дач"
                                        // Добавьте обработку других ваших вариантов
                                        else -> ""
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Добавьте логику, если ничего не выбрано
                                }
                            }
                    }
                    1 -> {
                        binding.spinnerOldOrNew.visibility = View.VISIBLE
                        binding.editRoom.visibility = View.VISIBLE
                        binding.Rooms.visibility = View.VISIBLE


                        val adapter = CustomSpinnerAdapter(requireContext(), itemsSend)
                        spinnerPodCategory.adapter = adapter
                        // Определите значения для подкатегорий этой категории
                        spinnerPodCategory.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedSubCategory = when (position) {
                                        0 -> "Продажа Квартир"
                                        1 -> "Продажа домов,дач"
                                        // Добавьте обработку других ваших вариантов
                                        else -> ""
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Добавьте логику, если ничего не выбрано
                                }
                            }
                    }
                    2 -> {
                        binding.spinnerOldOrNew.visibility = View.GONE
                        binding.editRoom.visibility = View.GONE
                        binding.Rooms.visibility = View.GONE


                        val adapter = CustomSpinnerAdapter(requireContext(), itemsWordSendBuy)
                        spinnerPodCategory.adapter = adapter
                        // Определите значения для подкатегорий этой категории
                        spinnerPodCategory.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedSubCategory = when (position) {
                                        0 -> "Аренда земель"
                                        1 -> "Продажа земель"
                                        // Добавьте обработку других ваших вариантов
                                        else -> ""
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Добавьте логику, если ничего не выбрано
                                }
                            }
                    }
                    3, 4 -> {
                        binding.spinnerOldOrNew.visibility = View.GONE
                        binding.editRoom.visibility = View.GONE
                        binding.Rooms.visibility = View.GONE

                        val adapter = CustomSpinnerAdapter(requireContext(), itemsObject)
                        spinnerPodCategory.adapter = adapter
                        // Определите значения для подкатегорий этих категорий
                        spinnerPodCategory.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedSubCategory = when (position) {
                                        0 -> "Аренда объектов"
                                        1 -> "Продажа объектов"
                                        // Добавьте обработку других ваших вариантов
                                        else -> ""
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Добавьте логику, если ничего не выбрано
                                }
                            }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ничего не делать
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
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        autoCompleteCity.setAdapter(adapter)
    }

    private fun uploadImagesToFirebaseStorage(
        title: String,
        price: String,
        city: String,
        desc: String,
        selectedCategory: String,
        selectedSubCategory: String,
        selectedOldNew: String,
        name: String,
        number: String,
        gmail: String,
        square: String,
        roomQuantity: String,
    ) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val selectedNodeRef = database.reference.child("products")
                .child(selectedCategory)
                .child(selectedSubCategory)

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
                                        selectedSubCategory,
                                        selectedOldNew,
                                        name,
                                        number,
                                        gmail,
                                        selectedNodeRef,
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


    private fun saveImageLinks(imageUrls: List<String>) {
        for (imageUrl in imageUrls) {
            val imageId = generateUniqueId(imageUrl)

            imageLinks[imageId] = imageUrl
        }
    }

    private fun generateUniqueId(imageUrl: String): String {
        return imageUrl.hashCode().toString()
    }


    private fun saveToDatabase(
        title: String,
        price: String,
        city: String,
        desc: String,
        selectedCategory: String,
        selectedSubCategory: String,
        selectedHome: String,
        name: String,
        number: String,
        gmail: String,
        selectedCategoryNodeRef: DatabaseReference,
        square: String,
        roomQuantity: String,


        ) {
        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)
            val adsRef = userRef.child("ads")


            val todayData = ServerValue.TIMESTAMP

            val product = hashMapOf(
                "title" to title,
                "price" to price,
                "city" to city,
                "desc" to desc,
                "category" to selectedCategory,
                "subCategory" to selectedSubCategory,
                "selectedHome" to selectedHome,
                "name" to name,
                "number" to number,
                "gmail" to gmail,
                "imageUrls" to imageLinks,
                "square" to square,
                "roomQuantity" to roomQuantity,
                "date" to todayData,
            )

            val newAdRef = adsRef.push()

            database = FirebaseDatabase.getInstance()

            val adminRef = database.reference.child("adminSimple").push()

            val productWithId = product + ("id" to adminRef.key)
            adminRef.setValue(productWithId)
                .addOnSuccessListener {
                    findNavController().navigate(R.id.action_newAdsFragment_to_okFragment)
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
                        "Ошибка сохранения данных ",
                        Toast.LENGTH_SHORT
                    ).show()
                    hud!!.dismiss()

                    binding.progressBar.visibility = View.GONE

                    Log.d("MyLog", "${it.message}")
                }

            newAdRef.setValue(productWithId)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Данные успешно сохранены!",
                        Toast.LENGTH_SHORT
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
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE

                    Log.d("MyLog", "${it.message}")
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Пользователь не аутентифицирован!",
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.visibility = View.GONE

        }
    }
}