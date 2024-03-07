package com.azerbaijankiraye.kirayeaz.Home


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.azerbaijankiraye.kirayeaz.*
import com.azerbaijankiraye.kirayeaz.Home.AdsPager.OnImageClickListener
import com.azerbaijankiraye.kirayeaz.Home.AdsPager.SlideshowFragment
import com.azerbaijankiraye.kirayeaz.Home.AdsPager.SlideshowPagerAdapter
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnImageClickListener {
    private val languageManager by lazy { LanguageManager(requireContext()) }


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllAdapter
    private var isLoading = false // Флаг, указывающий, что данные загружаются
    private var isLastPage = false // Флаг, указывающий, что это последняя страница
    private var currentPage = 1 // Текущая страница данных
    private val PAGE_SIZE = 10 // Количество элементов на странице
    private var backPressedOnce = false
    lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val currentLanguage = languageManager.getSavedLanguage()
        if (currentLanguage.isEmpty()) {
            languageManager.saveLanguage("az")
            languageManager.setLocale()
            activity!!.recreate()
        } else {
            // В противном случае установить сохраненный язык
            languageManager.setLocale()
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = binding.swipe
        initRc()
        vpInit()
        getDataFirebase()
        buttons()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Проверяем, если данные не загружаются, это не последняя страница и пользователь прокрутил вниз до конца списка
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        // Загружаем следующую порцию данных
                        isLoading = true
                        currentPage++
                        getDataFirebase()
                    }
                }
            }
        })

    }

    private fun buttons() {

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedOnce) {
                requireActivity().finish()
            } else {
                backPressedOnce = true
                Toast.makeText(requireContext(), getString(R.string.back), Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed(
                    { backPressedOnce = false },
                    2000
                ) // Сброс флага через 2 секунды
            }
        }
        callback.isEnabled = true
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                getDataFirebase()
            }
            bFilter.setOnClickListener {
                showFilterDialog()
            }
            val searchView = searchView
            searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchView.queryHint = getString(R.string.search)
                } else {
                    searchView.queryHint = getString(R.string.search)
                }
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Вызываем функцию для фильтрации данных
                    adapter.filter.filter(newText)
                    return true
                }
            })
        }
    }

    private fun showFilterDialog() {
        val filterOptions = arrayOf(
            getString(com.azerbaijankiraye.kirayeaz.R.string.home_dialog1),
            getString(R.string.home_dialog2),
            getString(R.string.home_dialog3)
        )

        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle(R.string.home_dialog_sort)
            .setItems(filterOptions) { dialog, which ->
                when (which) {
                    0 -> sortByDescendingPrices()
                    1 -> sortByAscendingPrices()
                    2 -> sortByDate()
                }
                dialog.dismiss()
            }

        builder.create().show()
    }


    private fun sortByDescendingPrices() {
        adapter.sortDataByDescendingPrices()
        adapter.notifyDataSetChanged()
    }

    private fun sortByAscendingPrices() {
        adapter.sortDataByAscendingPrices()
    }

    private fun sortByDate() {
        adapter.sortDataByDate()
    }


    private fun vpInit() {
        val viewPager = binding.vp2
        val indicator = binding.indikator
        val adapter = SlideshowPagerAdapter(requireActivity())

        viewPager.adapter = adapter

        viewPager.setPageTransformer { page, position ->
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        }
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val current = viewPager.currentItem
                val next = if (current + 1 < adapter.itemCount) current + 1 else 0
                viewPager.setCurrentItem(next, true)
                handler.postDelayed(this, 7000) // Автоматическое перемещение каждую секунду
            }
        }
        handler.postDelayed(runnable, 7000)
        indicator.setViewPager(viewPager)

        val fragmentList = adapter.fragments

        for (fragment in fragmentList) {
            if (fragment is SlideshowFragment) {
                fragment.setOnImageClickListener(this)
            }
        }


    }


    private fun getDataFirebase() {
        binding.loadingIndicator.visibility = View.VISIBLE

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("products")
        val referenceSimple = database.getReference("products").child("Для Обычных")


        val powerbankProducts = mutableListOf<UsersDataMy>()
        adapter.clear(powerbankProducts)

        val salesReference = reference.child("Для Маклеров").child("Продажа")
        fetchProductsFromCategory(salesReference, powerbankProducts)

        val salesReferenceAZ1 = reference.child("Для Маклеров").child("Satış")
        fetchProductsFromCategory(salesReferenceAZ1, powerbankProducts)

        val salesReferenceAZ = reference.child("Для Маклеров").child("Kirayə")
        fetchProductsFromCategory(salesReferenceAZ, powerbankProducts)

        val rentReference = reference.child("Для Маклеров").child("Аренда")
        fetchProductsFromCategory(rentReference, powerbankProducts)

        val students = reference.child("Для студентов").child("Ищу Квартиру")
        val students1 = reference.child("Для студентов").child("Сдаю Квартиру")
        val students2AZ = reference.child("Для студентов").child("Mənzil Axtarıram")
        val students1AZ = reference.child("Для студентов").child("Mənzil Kirayə Verirəm")


        fetchProductsFromCategory(students2AZ, powerbankProducts)
        fetchProductsFromCategory(students1AZ, powerbankProducts)
        fetchProductsFromCategory(students1, powerbankProducts)
        fetchProductsFromCategory(students, powerbankProducts)


        val a = "Аренды"
        val b = "Продажи"
        val c = "Участки земель"
        val d = "Объекты"

        val aZ = "Kirayə"
        val bZ = "Satış"
        val cZ = "Torpaq hissələri"
        val dZ = "Obyektlər"


        val a2Z = referenceSimple.child(aZ).child("Аренда Квартир")
        fetchProductsFromCategory(a2Z, powerbankProducts)

        val a3Z = referenceSimple.child(aZ).child("Аренда домов,дач")
        fetchProductsFromCategory(a3Z, powerbankProducts)

        val b2Z = referenceSimple.child(bZ).child("Продажа Квартир")
        fetchProductsFromCategory(b2Z, powerbankProducts)

        val b3Z = referenceSimple.child(bZ).child("Продажа домов,дач")
        fetchProductsFromCategory(b3Z, powerbankProducts)

        val c2Z = referenceSimple.child(cZ).child("Аренда земель")
        fetchProductsFromCategory(c2Z, powerbankProducts)

        val c3Z = referenceSimple.child(cZ).child("Продажа земель")
        fetchProductsFromCategory(c3Z, powerbankProducts)

        val d2Z = referenceSimple.child(dZ).child("Аренда объектов")
        fetchProductsFromCategory(d2Z, powerbankProducts)

        val d3Z = referenceSimple.child(dZ).child("Продажа объектов")
        fetchProductsFromCategory(d3Z, powerbankProducts)


        val a2 = referenceSimple.child(a).child("Аренда Квартир")
        fetchProductsFromCategory(a2, powerbankProducts)

        val a3 = referenceSimple.child(a).child("Аренда домов,дач")
        fetchProductsFromCategory(a3, powerbankProducts)

        val b2 = referenceSimple.child(b).child("Продажа Квартир")
        fetchProductsFromCategory(b2, powerbankProducts)

        val b3 = referenceSimple.child(b).child("Продажа домов,дач")
        fetchProductsFromCategory(b3, powerbankProducts)

        val c2 = referenceSimple.child(c).child("Аренда земель")
        fetchProductsFromCategory(c2, powerbankProducts)

        val c3 = referenceSimple.child(c).child("Продажа земель")
        fetchProductsFromCategory(c3, powerbankProducts)

        val d2 = referenceSimple.child(d).child("Аренда объектов")
        fetchProductsFromCategory(d2, powerbankProducts)

        val d3 = referenceSimple.child(d).child("Продажа объектов")
        fetchProductsFromCategory(d3, powerbankProducts)

        adapter.setData(powerbankProducts)
        swipeRefreshLayout.isRefreshing = false


    }

    private fun fetchProductsFromCategory(
        categoryReference: DatabaseReference,
        powerbankProducts: MutableList<UsersDataMy>,
    ) {
        categoryReference.orderByKey()
            .limitToFirst(PAGE_SIZE * currentPage)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        val title = productSnapshot.child("title").getValue(String::class.java)
                        val description = productSnapshot.child("desc").getValue(String::class.java)
                        val price = productSnapshot.child("price").getValue(String::class.java)
                        val city = productSnapshot.child("city").getValue(String::class.java)
                        val id = productSnapshot.child("id").getValue(String::class.java)

                        val selectedCategory =
                            productSnapshot.child("category").getValue(String::class.java)
                        val selectedSubCategory =
                            productSnapshot.child("subCategory").getValue(String::class.java)
                        val selectedHome =
                            productSnapshot.child("selectedHome").getValue(String::class.java)
                        val giveOrSearch =
                            productSnapshot.child("giveOreSearch").getValue(String::class.java)

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

                        val product = UsersDataMy(
                            id = id ?: "null",
                            nameOrg = nameOrg ?: "S",
                            title = title ?: "-",
                            price = price ?: "-",
                            city = city ?: "-",
                            desc = description ?: "-",
                            category = selectedCategory ?: "-",
                            subCategory = selectedSubCategory ?: "-",
                            giveOreSearch = giveOrSearch ?: "-",
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

                    if (powerbankProducts.size < PAGE_SIZE * currentPage) {
                        isLastPage = true
                    }
                    binding.loadingIndicator.visibility = View.GONE
                    adapter.setData(powerbankProducts)
                    binding.progressBar1.visibility = View.GONE
                    binding.indikator.visibility = View.VISIBLE
                    binding.vp2.visibility = View.VISIBLE
                    binding.textView6.visibility = View.VISIBLE
                    binding.nestedScrollView2.visibility = View.VISIBLE
                    binding.searchView.visibility = View.VISIBLE
                    binding.bFilter.visibility = View.VISIBLE
                    isLoading = false


                }

                override fun onCancelled(error: DatabaseError) {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.progressBar1.visibility = View.GONE
                    binding.indikator.visibility = View.GONE
                    binding.textView6.visibility = View.GONE
                    binding.nestedScrollView2.visibility = View.GONE
                    binding.searchView.visibility = View.GONE
                    binding.bFilter.visibility = View.GONE
                    Toast.makeText(requireContext(), "Internet Error", Toast.LENGTH_LONG).show()
                    isLoading = false
                }
            })
    }


    private fun initRc() {
        adapter = AllAdapter(requireContext())
        recyclerView = binding.rc
        val layoutManager = GridLayoutManager(
            requireContext(),
            2
        ) // Измените 2 на количество столбцов в вашем сетке
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.onItemClick = { adminData ->
            val intent = Intent(requireContext(), HomeDetailActivity::class.java)
            intent.putExtra("data", adminData)
            startActivity(intent)
        }
    }


    override fun onImageClick(position: Int) {
        when (position) {
            0 -> findNavController().navigate(R.id.action_action_home_to_maklersAllFragment)
            1 -> findNavController().navigate(R.id.action_action_home_to_studentsAllFragment)
            2 -> startActivity(Intent(requireContext(), WeActivity::class.java))
        }
    }

}