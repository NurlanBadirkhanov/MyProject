package com.azerbaijankiraye.kirayeaz.Category.Category.SendHome.SendScreens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.azerbaijankiraye.kirayeaz.AllAdapter
import com.azerbaijankiraye.kirayeaz.Home.HomeDetailActivity
import com.azerbaijankiraye.kirayeaz.UsersDataMy
import com.azerbaijankiraye.kirayeaz.databinding.FragmentAllSendBinding
import com.google.firebase.database.*


class AllSendFragment : Fragment() {
    lateinit var binding: FragmentAllSendBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllSendBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
        getFirebase()
        binding.bBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getFirebase() {
        val database = FirebaseDatabase.getInstance()
        val powerbankProducts = mutableListOf<UsersDataMy>()

        val referenceSimple = database.getReference("products").child("Для Обычных")


        val b = "Продажи"
        val bZ = "Satış"


        val b2Z = referenceSimple.child(bZ).child("Продажа Квартир")
        fetchProductsFromCategory(b2Z, powerbankProducts)

        val b3Z = referenceSimple.child(bZ).child("Продажа домов,дач")
        fetchProductsFromCategory(b3Z, powerbankProducts)


        val b2 = referenceSimple.child(b).child("Продажа Квартир")
        fetchProductsFromCategory(b2, powerbankProducts)

        val b3 = referenceSimple.child(b).child("Продажа домов,дач")
        fetchProductsFromCategory(b3, powerbankProducts)
        adapter.setData(powerbankProducts)
    }

    private fun fetchProductsFromCategory(
        categoryReference: DatabaseReference,
        powerbankProducts: MutableList<UsersDataMy>,
    ) {
        categoryReference.orderByKey()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        val title = productSnapshot.child("title").getValue(String::class.java)
                        val description = productSnapshot.child("desc").getValue(String::class.java)
                        val price = productSnapshot.child("price").getValue(String::class.java)
                        val city = productSnapshot.child("city").getValue(String::class.java)
                        val selectedCategory =
                            productSnapshot.child("category").getValue(String::class.java)
                        val selectedSubCategory =
                            productSnapshot.child("subCategory").getValue(String::class.java)
                        val selectedHome = productSnapshot.child("selectedHome").getValue(String::class.java)
                        val giveOrSearch = productSnapshot.child("giveOreSearch").getValue(String::class.java)

                        val name = productSnapshot.child("name").getValue(String::class.java)
                        val number = productSnapshot.child("number").getValue(String::class.java)
                        val gmail = productSnapshot.child("gmail").getValue(String::class.java)
                        val square = productSnapshot.child("square").getValue(String::class.java)
                        val roomQuantity =
                            productSnapshot.child("roomQuantity").getValue(String::class.java)

                        val nameOrg = productSnapshot.child("nameOrg").getValue(String::class.java)

                        val date = productSnapshot.child("date").getValue(Long::class.java)

                        val imagesSnapshot = productSnapshot.child("imageUrls")
                        val currentImgUrlList = mutableListOf<String>()
                        for (imageSnapshot in imagesSnapshot.children) {
                            val imageUrl = imageSnapshot.getValue(String::class.java)
                            currentImgUrlList.add(imageUrl ?: "")
                        }
                        val id = productSnapshot.child("id").getValue(String::class.java)


                        val product = UsersDataMy(
                            id = id?:"null",
                            nameOrg = nameOrg?:"S",
                            title = title ?: "-",
                            price = price ?: "-",
                            city = city ?: "-",
                            desc = description ?: "-",
                            category = selectedCategory ?: "-",
                            subCategory = selectedSubCategory?: "-",
                            giveOreSearch = giveOrSearch?: "-",
                            selectedHome = selectedHome ?: "-",
                            name = name ?: "-",
                            number = number ?: "-",
                            gmail = gmail ?: "-",
                            square = square ?: "",
                            roomQuantity = roomQuantity ?: "-",
                            imageUrls = currentImgUrlList,
                            date = date ?: 0,
                        )
                        powerbankProducts.add(product)
                    }

                    adapter.setData(powerbankProducts)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Internet Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun initRc() {
        adapter = AllAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        adapter.onItemClick = { adminData ->
            val intent = Intent(requireContext(), HomeDetailActivity::class.java)
            intent.putExtra("data", adminData)
            startActivity(intent)
        }

    }
}


