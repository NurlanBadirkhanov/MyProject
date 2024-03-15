package com.ilnodstudio.ansartelecom.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.ilnodstudio.ansartelecom.databinding.FragmentAdminNewBinding

class AdminNewFragment : Fragment() {
    lateinit var binding: FragmentAdminNewBinding
    private val PICK_IMAGE_REQUEST = 1
    private val MAX_IMAGES = 5
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var imSelected: ImageView
    private lateinit var imSelected1: ImageView
    private lateinit var imSelected2: ImageView
    private lateinit var imSelected3: ImageView
    private lateinit var imSelected4: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminNewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         imSelected =  binding.imSelected
         imSelected1 = binding.imSelected1
         imSelected2 = binding.imSelected2
         imSelected3 = binding.imSelected3
         imSelected4 = binding.imSelected4
        spinner()

        b()


        binding.bPhoto.setOnClickListener {
            if (selectedImages.size < MAX_IMAGES) {
                openImagePicker()
            } else {
                Toast.makeText(requireContext(), "Максимум $MAX_IMAGES фотографий", Toast.LENGTH_SHORT).show()
            }
        }
        binding.addButton.setOnClickListener {

            val category = "Ansar"
            val mainCategory = binding.spinner.selectedItem?.toString() ?: ""
            val subCategory = binding.subCategorySpinner.selectedItem?.toString() ?: ""
            val model = binding.spinner3.selectedItem?.toString() ?: ""
            val title = binding.editTitle.text?.toString() ?: ""
            val price = binding.edPrice.text?.toString() ?: ""
            val description = binding.editDesc.text?.toString() ?: ""

            if (title.isEmpty() || price.isEmpty() || description.isEmpty()) {
                 Toast.makeText(requireContext(), "Мика  заполни все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveToFirebase(category, mainCategory, subCategory, model, title, price, description)
            binding.spinner.setSelection(0)
            binding.editTitle.text?.clear()
            binding.edPrice.text?.clear()
            binding.editDesc.text?.clear()
            selectedImages.clear()
            val imageViews = arrayOf(imSelected, imSelected1, imSelected2, imSelected3, imSelected4)
            for (imageView in imageViews) {
                imageView.visibility = View.GONE
            }
        }

    }

    private fun b() {
        binding.bToListAdmin.setOnClickListener {
        val navController = findNavController()
         navController.popBackStack()
        }
    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
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

    private fun spinner() {
        val mainCategories = listOf("Электроника", "Аксесуары", "Сим-Карты")

        val mainAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mainCategories)
        mainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = mainAdapter

        val smartphoneModels = mutableListOf<String>()
        binding.spinner3.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, smartphoneModels)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMainCategory = mainCategories[position]
                when (selectedMainCategory) {
                    "Электроника" -> showSubCategories(listOf("Планшеты", "Мобильные Телефоны", "Часы","Ноутбуки"))
                    "Аксесуары" -> showSubCategories(listOf("ПоверБанки","Чехлы", "Наушники","Винилы","Защитные стекла","Адаптеры","USB-Шнуры","Флеш карты","Другое"))
                    "Сим-Карты" -> showSubCategories(listOf("Bakcell", "Azercell", "Nar"))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // ...
            }
        }

        binding.subCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSubCategory = binding.subCategorySpinner.getItemAtPosition(position).toString()
                if (selectedSubCategory == "Мобильные Телефоны") {
                    smartphoneModels.clear()
                    smartphoneModels.addAll(listOf("Samsung", "Apple (iPhone)", "Xiaomi", "Huawei", "OnePlus", "Google", "Sony", "LG", "Motorola", "Nokia", "Asus", "Realme", "Oppo", "Vivo", "Lenovo", "ZTE", "Meizu", "BlackBerry", "Honor", "HTC", "Alcatel", "TCL", "Xolo", "Panasonic", "Micromax", "Gionee", "Lava", "Infinix", "Coolpad", "Yu", "LeEco"))

                    val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, smartphoneModels)
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinner3.adapter = modelAdapter
                } else {
                    smartphoneModels.clear()
                    binding.spinner3.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, emptyList<String>())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun showSubCategories(subCategories: List<String>) {
        val subAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subCategories)
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.subCategorySpinner.adapter = subAdapter
    }

    private fun saveToFirebase(category: String, mainCategory: String, subCategory: String, model: String, title: String, price: String, description: String) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")

        val categoryReference = reference.child(category)
        val mainCategoryReference = categoryReference.child(mainCategory)
        val subCategoryReference = mainCategoryReference.child(subCategory)

        val modelReference = subCategoryReference.child(model)

        val productKey = modelReference.push().key

        if (productKey != null) {
            val productData = mapOf(
                "title" to title,
                "description" to description,
                "price" to price
            )

            val storageReference = FirebaseStorage.getInstance().reference
            val productImagesReference = storageReference.child("product_images").child(productKey)

            for (i in selectedImages.indices) {
                val imageUri = selectedImages[i]
                val imageName = "image_$i"
                val imageRef = productImagesReference.child(imageName)

                imageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                            val imageDatabaseRef = modelReference.child(productKey).child("images").child(imageName)
                            imageDatabaseRef.setValue(imageUrl.toString())
                        }
                    }
                    .addOnFailureListener { e ->
                        // Обработка ошибок при загрузке изображения
                    }
            }

            modelReference.child(productKey).setValue(productData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Успешно!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Не удалось!", Toast.LENGTH_SHORT).show()
                }
        }
    }



}
