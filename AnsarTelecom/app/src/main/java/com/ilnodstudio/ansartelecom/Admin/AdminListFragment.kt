package com.ilnodstudio.ansartelecom.Admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentAdminListBinding


class AdminListFragment : Fragment() {
   lateinit var binding:FragmentAdminListBinding
   private lateinit var adapter: AdminAdapter
   private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        b()
        loadDataFromFirebase()
    }


    private fun loadDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allAdverts = mutableListOf<AdminData>()

                for (categorySnapshot in snapshot.children) {
                    for (mainCategorySnapshot in categorySnapshot.children) {
                        for (subCategorySnapshot in mainCategorySnapshot.children) {
                            for (modelSnapshot in subCategorySnapshot.children) {
                                val description = modelSnapshot.child("description").getValue(String::class.java)
                                val priceValue = modelSnapshot.child("price").getValue()
                                val price = if (priceValue is String) {
                                    priceValue.toIntOrNull() ?: 0
                                } else {
                                    priceValue as? Int ?: 0
                                }
                                val title = modelSnapshot.child("title").getValue(String::class.java)
                                val imagesSnapshot = modelSnapshot.child("images")

                                val currentImgUrlList = mutableListOf<String>()
                                for (imageSnapshot in imagesSnapshot.children) {
                                    val imageUrl = imageSnapshot.getValue(String::class.java)
                                    currentImgUrlList.add(imageUrl ?: "")
                                }

                                if (title != null) {
                                    val itemKey = modelSnapshot.key // Получаем уникальный ключ текущего элемента
                                    val advert = AdminData(
                                        title = title,
                                        Price = price ?: 0,
                                        description = description ?: "",
                                        imgUrlList = currentImgUrlList,
                                        itemKey = itemKey ?: ""
                                    )
                                    allAdverts.add(advert)
                                }
                            }
                        }
                    }
                }

                val mobilePhonesAdverts = mutableListOf<AdminData>()

                val ansarSnapshot = snapshot.child("Ansar")
                val electronicsSnapshot = ansarSnapshot.child("Электроника")
                val mobilePhonesSnapshot = electronicsSnapshot.child("Мобильные Телефоны")

                for (modelSnapshot in mobilePhonesSnapshot.children) {
                    for (itemSnapshot in modelSnapshot.children) {
                        val description = itemSnapshot.child("description").getValue(String::class.java)
                        val priceValue = itemSnapshot.child("price").getValue()
                        val price = if (priceValue is String) {
                            priceValue.toIntOrNull() ?: 0
                        } else {
                            priceValue as? Int ?: 0
                        }
                        val title = itemSnapshot.child("title").getValue(String::class.java)
                        val imagesSnapshot = itemSnapshot.child("images")

                        val currentImgUrlList = mutableListOf<String>()
                        for (imageSnapshot in imagesSnapshot.children) {
                            val imageUrl = imageSnapshot.getValue(String::class.java)
                            currentImgUrlList.add(imageUrl ?: "")
                        }

                        if (title != null) {
                            val itemKey = itemSnapshot.key // Получаем уникальный ключ текущего элемента
                            val advert = AdminData(
                                title = title,
                                Price = price ?: 0,
                                description = description ?: "",
                                imgUrlList = currentImgUrlList,
                                itemKey = itemKey ?: ""
                            )
                            mobilePhonesAdverts.add(advert)
                        }
                    }
                }

                allAdverts.addAll(mobilePhonesAdverts)

                // Устанавливаем данные для адаптера только один раз в конце
                adapter.setData(allAdverts)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun b() {
        binding.floatingActionButton2.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_adminListFragment_to_adminNewFragment)
        }
    }

    private fun init() {
        adapter = AdminAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        adapter.onItemClick = { adminData ->
            val intent = Intent(requireContext(), AdminListActivityDirections::class.java)
            intent.putExtra("data", adminData)
            startActivity(intent)
        }



    }


}