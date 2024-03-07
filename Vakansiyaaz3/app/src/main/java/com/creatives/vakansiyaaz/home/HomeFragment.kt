package com.creatives.vakansiyaaz.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.FragmentHomeBinding
import com.creatives.vakansiyaaz.home.adapter.HomeAdapter
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        initRc()
        getDataFromFirebase()
        auth = FirebaseAuth.getInstance()

    }
    private fun getDataFromFirebase() {
        CoroutineScope(Dispatchers.Main).launch {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Vakancies").child("Vakancies")
            val product = mutableListOf<VacancyData>()
            val progressBar = binding.progressBar
            fetchProductsFromCategory1(adapter, requireContext(), reference, product, progressBar)
            binding.swipr.setOnRefreshListener {
                product.clear()
                CoroutineScope(Dispatchers.Main).launch {
                    fetchProductsFromCategory1(
                        adapter,
                        requireContext(),
                        reference,
                        product,
                        progressBar

                    )
                    binding.swipr.isRefreshing = false


                }
            }
        }

    }


    private fun initRc() {
        adapter = HomeAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
//        adapter.setData(Utils().fakeVacanciesList)

        adapter.onItemClick = { data ->
            val intent = Intent(requireContext(), ScreenDetailActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }

    }


}