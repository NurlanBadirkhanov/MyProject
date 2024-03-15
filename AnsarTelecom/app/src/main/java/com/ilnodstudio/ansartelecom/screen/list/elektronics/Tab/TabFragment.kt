package com.ilnodstudio.ansartelecom.screen.list.elektronics.Tab

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
import com.ilnodstudio.ansartelecom.MAIN
import com.ilnodstudio.ansartelecom.databinding.FragmentTabBinding
import com.ilnodstudio.ansartelecom.screen.home.DetailHomeActivity
import com.ilnodstudio.ansartelecom.screen.home.HomeModel

class TabFragment : Fragment() {
    lateinit var binding: FragmentTabBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentTabBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        b()
        downloadPowerbankProducts()
        MAIN.binding.openMenu.visibility = View.GONE

    }

        private fun b() {
            binding.bExitToList.setOnClickListener{
                val navController = findNavController()
                navController.popBackStack()

            }

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
    private fun downloadPowerbankProducts() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")

        val ansarSnapshot = reference.child("Ansar")
        val accessoriesSnapshot = ansarSnapshot.child("Электроника")
        val powerbankSnapshot = accessoriesSnapshot.child("Планшеты")

        val powerbankProducts = mutableListOf<HomeModel>()

        powerbankSnapshot.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (productSnapshot in snapshot.children) {
                    val title = productSnapshot.child("title").getValue(String::class.java)
                    val description = productSnapshot.child("description").getValue(String::class.java)
                    val price = productSnapshot.child("price").getValue(String::class.java)
                    val images = mutableListOf<String>()

                    // Получаем список URL-ов изображений
                    for (imageSnapshot in productSnapshot.child("images").children) {
                        val imageUrl = imageSnapshot.getValue(String::class.java)
                        imageUrl?.let { images.add(it) }
                    }

                    val product = HomeModel(
                        title = title ?: "",
                        description = description ?: "",
                        Price = price?.toIntOrNull() ?: 0,
                        imgUrlList = images
                    )
                    powerbankProducts.add(product)
                }

                // Передаем данные в адаптер
                adapter.setData(powerbankProducts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки, если необходимо
            }
        })
    }


}