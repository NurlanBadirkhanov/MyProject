package com.azerbaijankiraye.kirayeaz.Profile.Admin

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.azerbaijankiraye.kirayeaz.AllAdapter
import com.azerbaijankiraye.kirayeaz.UsersDataMy
import com.azerbaijankiraye.kirayeaz.databinding.FragmentAdminPanelBinding

class AdminPanelFragment : Fragment() {
    private lateinit var binding:FragmentAdminPanelBinding
    private lateinit var adapter: AllAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminPanelBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
        getDataFirebase()
        binding.button3.setOnClickListener {
            findNavController().navigate(com.azerbaijankiraye.kirayeaz.R.id.action_adminPanelFragment_to_adminNewAdsPersonFragment)
        }

    }
    private fun initRc() {
        adapter = AllAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
    }

    private fun getDataFirebase() {

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")
        val referenceSimple = database.getReference("products").child("Для Обычных")


        val powerbankProducts = mutableListOf<UsersDataMy>()

        val salesReference = reference.child("Для Маклеров").child("Продажа")
        fetchProductsFromCategory(salesReference, powerbankProducts,0)

        val salesReferenceAZ1 = reference.child("Для Маклеров").child("Satış")
        fetchProductsFromCategory(salesReferenceAZ1, powerbankProducts,0)

        val salesReferenceAZ = reference.child("Для Маклеров").child("Kirayə")
        fetchProductsFromCategory(salesReferenceAZ, powerbankProducts,0)

        val rentReference = reference.child("Для Маклеров").child("Аренда")
        fetchProductsFromCategory(rentReference, powerbankProducts,0)

        val students = reference.child("Для студентов").child("Ищу Квартиру")
        val students1 = reference.child("Для студентов").child("Сдаю Квартиру")
        val students2AZ = reference.child("Для студентов").child("Mənzil Axtarıram")
        val students1AZ = reference.child("Для студентов").child("Mənzil Kirayə Verirəm")


        fetchProductsFromCategory(students2AZ, powerbankProducts,0)
        fetchProductsFromCategory(students1AZ, powerbankProducts,0)
        fetchProductsFromCategory(students1, powerbankProducts,0)
        fetchProductsFromCategory(students, powerbankProducts,0)


        val a = "Аренды"
        val b = "Продажи"
        val c = "Участки земель"
        val d = "Объекты"
        val all = "Все объявления категории"

        val aZ = "Kirayə"
        val bZ = "Satış"
        val cZ = "Torpaq hissələri"
        val dZ = "Obyektlər"



        //Аренды а1 = Аренда Если ты не я то врядли поймешь)))
        val a1Z = referenceSimple.child(aZ).child(all)
        fetchProductsFromCategory(a1Z, powerbankProducts,0)

        val a2Z = referenceSimple.child(aZ).child("Аренда Квартир")
        fetchProductsFromCategory(a2Z, powerbankProducts,0)

        val a3Z = referenceSimple.child(aZ).child("Аренда домов,дач")
        fetchProductsFromCategory(a3Z, powerbankProducts,0)
        /////
        val b1Z = referenceSimple.child(bZ).child(all)
        fetchProductsFromCategory(b1Z, powerbankProducts,0)

        val b2Z = referenceSimple.child(bZ).child("Продажа Квартир")
        fetchProductsFromCategory(b2Z, powerbankProducts,0)

        val b3Z = referenceSimple.child(bZ).child("Продажа домов,дач")
        fetchProductsFromCategory(b3Z, powerbankProducts,0)
        /////
        val c1Z = referenceSimple.child(cZ).child(all)
        fetchProductsFromCategory(c1Z, powerbankProducts,0)

        val c2Z = referenceSimple.child(cZ).child("Аренда земель")
        fetchProductsFromCategory(c2Z, powerbankProducts,0)

        val c3Z = referenceSimple.child(cZ).child("Продажа земель")
        fetchProductsFromCategory(c3Z, powerbankProducts,0)
        /////
        val d1Z = referenceSimple.child(dZ).child(all)
        fetchProductsFromCategory(d1Z, powerbankProducts,0)

        val d2Z = referenceSimple.child(dZ).child("Аренда объектов")
        fetchProductsFromCategory(d2Z, powerbankProducts,0)

        val d3Z = referenceSimple.child(dZ).child("Продажа объектов")
        fetchProductsFromCategory(d3Z, powerbankProducts,0)



        //Аренды а1 = Аренда Если ты не я то врядли поймешь)))
        val a1 = referenceSimple.child(a).child(all)
        fetchProductsFromCategory(a1, powerbankProducts,0)

        val a2 = referenceSimple.child(a).child("Аренда Квартир")
        fetchProductsFromCategory(a2, powerbankProducts,0)

        val a3 = referenceSimple.child(a).child("Аренда домов,дач")
        fetchProductsFromCategory(a3, powerbankProducts,0)
        /////
        val b1 = referenceSimple.child(b).child(all)
        fetchProductsFromCategory(b1, powerbankProducts,0)

        val b2 = referenceSimple.child(b).child("Продажа Квартир")
        fetchProductsFromCategory(b2, powerbankProducts,0)

        val b3 = referenceSimple.child(b).child("Продажа домов,дач")
        fetchProductsFromCategory(b3, powerbankProducts,0)
        /////
        val c1 = referenceSimple.child(c).child(all)
        fetchProductsFromCategory(c1, powerbankProducts,0)

        val c2 = referenceSimple.child(c).child("Аренда земель")
        fetchProductsFromCategory(c2, powerbankProducts,0)

        val c3 = referenceSimple.child(c).child("Продажа земель")
        fetchProductsFromCategory(c3, powerbankProducts,0)
        /////
        val d1 = referenceSimple.child(d).child(all)
        fetchProductsFromCategory(d1, powerbankProducts,0)

        val d2 = referenceSimple.child(d).child("Аренда объектов")
        fetchProductsFromCategory(d2, powerbankProducts,0)

        val d3 = referenceSimple.child(d).child("Продажа объектов")
        fetchProductsFromCategory(d3, powerbankProducts,0)



        adapter.setData(powerbankProducts)

    }

    private fun fetchProductsFromCategory(
        categoryReference: DatabaseReference,
        powerbankProducts: MutableList<UsersDataMy>,
        offset: Int
    ) {
        categoryReference.orderByKey()
            .startAt((offset).toString())
            .limitToFirst(PAGE_SIZE)
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
                    binding.progressBar2.visibility = View.GONE




                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Internet Error", Toast.LENGTH_LONG).show()
                }
            })
    }


}