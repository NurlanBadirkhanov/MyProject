package com.ilnodstudio.ansartelecom.screen.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ilnodstudio.ansartelecom.Admin.HomeAdapter
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private val allAdverts = mutableListOf<HomeModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
        loadDataFromFirebase()
        searchView()
        setSearchViewTextColor(binding.searchView, R.color.black)
    }

    private fun setSearchViewTextColor(searchView: SearchView, colorRes: Int) {
        try {
            val searchField = SearchView::class.java.getDeclaredField("mSearchSrcTextView")
            searchField.isAccessible = true
            val searchAutoComplete = searchField.get(searchView) as AutoCompleteTextView
            searchAutoComplete.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
            searchAutoComplete.setHintTextColor(ContextCompat.getColor(requireContext(), colorRes))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun loadDataFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allAdverts.clear()

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
                                    val itemKey = modelSnapshot.key
                                    val advert = HomeModel(
                                        title = title,
                                        Price = price,
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

                val mobilePhonesAdverts = mutableListOf<HomeModel>()

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
                            val itemKey = itemSnapshot.key
                            val advert = HomeModel(
                                title = title,
                                Price = price,
                                description = description ?: "",
                                imgUrlList = currentImgUrlList,
                                itemKey = itemKey ?: ""
                            )
                            mobilePhonesAdverts.add(advert)
                        }
                    }
                }

                allAdverts.addAll(mobilePhonesAdverts)

                adapter.setData(allAdverts)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun searchView() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    adapter.setData(allAdverts)
                } else {
                    val filteredList = allAdverts.filter { advert ->
                        advert.title.contains(newText, ignoreCase = true)
                    }
                    adapter.setData(filteredList)
                }
                return true
            }
        })
    }

    private fun initRc() {
        adapter = HomeAdapter(requireContext())
        recyclerView = binding.RcView
        recyclerView.adapter = adapter

        adapter.onItemClick = { homedata ->
            val intent = Intent(requireContext(), DetailHomeActivity::class.java)
            intent.putExtra("data", homedata)
            startActivity(intent)
        }
    }
}
