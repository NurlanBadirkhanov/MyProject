package com.ilnodstudio.ansartelecom.screen.list.elektronics.SmartPhon

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ilnodstudio.ansartelecom.Admin.HomeAdapter
import com.ilnodstudio.ansartelecom.Admin.SmartAdapter
import com.ilnodstudio.ansartelecom.MAIN
import com.ilnodstudio.ansartelecom.databinding.FragmentSmartPhonBinding
import com.ilnodstudio.ansartelecom.screen.home.DetailHomeActivity
import com.ilnodstudio.ansartelecom.screen.home.HomeModel


class SmartPhonFragment : Fragment() {
    lateinit var binding:FragmentSmartPhonBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSmartPhonBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b()
        init()
        downloadSmartPhoneProducts()
        MAIN.binding.openMenu.visibility = View.GONE

    }

    private fun init() {
        adapter = HomeAdapter(requireContext())
        recyclerView = binding.rcMob
        recyclerView.adapter = adapter

        adapter.onItemClick = { homedata ->
            val intent = Intent(requireContext(), DetailHomeActivity::class.java)
            intent.putExtra("data",homedata )
            startActivity(intent)
        }

    }

    private fun b() {
        binding.apply {
            bExitToList.setOnClickListener {
                val navController = findNavController()
                navController.popBackStack()
            }
        }
    }
    private fun downloadSmartPhoneProducts() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")

        val ansarSnapshot = reference.child("Ansar")
        val electronicsSnapshot = ansarSnapshot.child("Электроника")
        val mobilePhonesSnapshot = electronicsSnapshot.child("Мобильные Телефоны")

        val smartPhoneProducts = mutableListOf<HomeModel>()

        mobilePhonesSnapshot.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (brandSnapshot in snapshot.children) {
                    val brandName = brandSnapshot.key

                    for (modelSnapshot in brandSnapshot.children) {
                        val title = modelSnapshot.child("title").getValue(String::class.java)
                        val price = modelSnapshot.child("price").getValue(String::class.java)
                        val images = mutableListOf<String>()

                        for (imageSnapshot in modelSnapshot.child("images").children) {
                            val imageUrl = imageSnapshot.getValue(String::class.java)
                            imageUrl?.let { images.add(it) }
                        }

                        val description = modelSnapshot.child("description").getValue(String::class.java) // Əlavə etdiyiniz xüsusiyyəti əldə edin

                        val product = HomeModel(
                            brandName = brandName ?: "",
                            category = "Мобильные Телефоны",
                            model = modelSnapshot.key ?: "",
                            title = title ?: "",
                            Price = price?.toIntOrNull() ?: 0,
                            imgUrlList = images,
                            description = description ?: "" // Əldə etdiyiniz description-ı istifadə edin
                        )
                        smartPhoneProducts.add(product)
                    }
                }

                adapter.setData(smartPhoneProducts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Ləğv edildikdən sonra ediləcək işlər
            }
        })
    }





}