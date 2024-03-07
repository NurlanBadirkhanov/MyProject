package com.azerbaijankiraye.kirayeaz.Profile.Admin.AdminScreens

import android.content.Intent
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.*
import com.azerbaijankiraye.kirayeaz.AllAdapter
import com.azerbaijankiraye.kirayeaz.Profile.Admin.Details.MaklersDetailActivity
import com.azerbaijankiraye.kirayeaz.UsersDataMy
import com.azerbaijankiraye.kirayeaz.databinding.FragmentMaklers2Binding


class MaklersFragment : Fragment() {
    lateinit var binding: FragmentMaklers2Binding
    private lateinit var adapter: AllAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaklers2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = binding.swipe
        initRc()
        getFirebase()

        swipeRefreshLayout.setOnRefreshListener {
            getFirebase()
        }

    }

    private fun getFirebase() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("adminMaklers")
        val powerbankProducts = mutableListOf<UsersDataMy>()
        fetchProductsFromCategory(reference, powerbankProducts,0)
        adapter.setData(powerbankProducts)
        swipeRefreshLayout.isRefreshing = false



    }

    private fun fetchProductsFromCategory(
        categoryReference: DatabaseReference,
        powerbankProducts: MutableList<UsersDataMy>,
        page: Int
    ) {
        val pageSize = MifareUltralight.PAGE_SIZE.toLong() // Переводим PAGE_SIZE в Long

        categoryReference.orderByKey()
            .startAt(((page - 1) * pageSize).toString())  // Convert to String
            .limitToFirst(MifareUltralight.PAGE_SIZE)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        val title =
                            productSnapshot.child("title").getValue(String::class.java)
                        val description =
                            productSnapshot.child("desc").getValue(String::class.java)
                        val price =
                            productSnapshot.child("price").getValue(String::class.java)
                        val id = productSnapshot.child("id").getValue(String::class.java)

                        val city =
                            productSnapshot.child("city").getValue(String::class.java)
                        val selectedCategory =
                            productSnapshot.child("category").getValue(String::class.java)
                        val selectedSubCategory =
                            productSnapshot.child("subCategory").getValue(String::class.java)
                        val selectedHome =
                            productSnapshot.child("selectedHome").getValue(String::class.java)
                        val name =
                            productSnapshot.child("name").getValue(String::class.java)
                        val number =
                            productSnapshot.child("number").getValue(String::class.java)
                        val gmail =
                            productSnapshot.child("gmail").getValue(String::class.java)
                        val square =
                            productSnapshot.child("square").getValue(String::class.java)
                        val nameOrg =
                            productSnapshot.child("nameOrg").getValue(String::class.java)
                        val roomQuantity =
                            productSnapshot.child("roomQuantity").getValue(String::class.java)
                        val date =
                            productSnapshot.child("date").getValue(Long::class.java)
                        val images = mutableListOf<String>()
                        for (imageSnapshot in productSnapshot.child("imageUrls").children) {
                            val imageUrl = imageSnapshot.getValue(String::class.java)
                            imageUrl?.let { images.add(it) }
                        }

                        val product = UsersDataMy(
                            id = id ?: "",
                            nameOrg = nameOrg ?: "",
                            title = title ?: "",
                            price = price ?: "",
                            city = city ?: "",
                            desc = description ?: "",
                            category = selectedCategory ?: "",
                            subCategory = selectedSubCategory ?: "",
                            selectedHome = selectedHome ?: "",
                            name = name ?: "",
                            number = number ?: "",
                            gmail = gmail ?: "",
                            square = square ?: "",
                            roomQuantity = roomQuantity ?: "",
                            imageUrls = images.toList(),
                            date = date ?: 0,
                        )
                        powerbankProducts.add(product)
                    }

                    adapter.setData(powerbankProducts)
                    adapter.notifyDataSetChanged()
                    binding.progressBar2.visibility = View.GONE
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
            val intent = Intent(requireContext(), MaklersDetailActivity::class.java)
            intent.putExtra("data", adminData)
            startActivity(intent)
        }
    }


}