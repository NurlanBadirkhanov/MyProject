package com.azerbaijankiraye.kirayeaz.Profile.TabViewPager


import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.azerbaijankiraye.kirayeaz.AllAdapter
import com.azerbaijankiraye.kirayeaz.Home.HomeDetailActivity
import com.azerbaijankiraye.kirayeaz.UsersDataMy
import com.azerbaijankiraye.kirayeaz.databinding.FragmentMyAdsBinding

class MyAdsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMyAdsBinding
    private lateinit var adapter: AllAdapter
    private lateinit var recyclerView: RecyclerView
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAdsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("users").child(auth.uid.toString()).child("ads")
        val powerbankProducts = mutableListOf<UsersDataMy>()

        fetchProductsFromCategory(reference, powerbankProducts)

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
                        val name = productSnapshot.child("name").getValue(String::class.java)
                        val number = productSnapshot.child("number").getValue(String::class.java)
                        val gmail = productSnapshot.child("gmail").getValue(String::class.java)
                        val square = productSnapshot.child("square").getValue(String::class.java)
                        val roomQuantity =
                            productSnapshot.child("roomQuantity").getValue(String::class.java)
                        val date = productSnapshot.child("date").getValue(Long::class.java)
                        val images = mutableListOf<String>()
                        for (imageSnapshot in productSnapshot.child("imageUrls").children) {
                            val imageUrl = imageSnapshot.getValue(String::class.java)
                            imageUrl?.let { images.add(it) }
                        }

                        val product = UsersDataMy(
                            title = title ?: "-",
                            price = price ?: "-",
                            city = city ?: "-",
                            desc = description ?: "-",
                            category = selectedCategory ?: "-",
                            subCategory = selectedSubCategory?: "-",
                            selectedHome = selectedHome ?: "-",
                            name = name ?: "-",
                            number = number ?: "-",
                            gmail = gmail ?: "-",
                            square = square ?: "",
                            roomQuantity = roomQuantity ?: "-",
                            imageUrls = images,
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
