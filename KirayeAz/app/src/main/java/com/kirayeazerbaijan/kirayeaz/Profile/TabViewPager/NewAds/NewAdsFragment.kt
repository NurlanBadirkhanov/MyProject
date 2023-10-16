package com.kirayeazerbaijan.kirayeaz.Profile.TabViewPager.NewAds

import android.R
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.kirayeazerbaijan.kirayeaz.AllAdapter
import com.kirayeazerbaijan.kirayeaz.AllData
import com.kirayeazerbaijan.kirayeaz.DaggerHilt2.InitFirebaseViewModel
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentNewAdsBinding
import javax.inject.Inject


class NewAdsFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentNewAdsBinding
    lateinit var adapter: AllAdapter
    @Inject
    lateinit var database: FirebaseDatabase
    val model: InitFirebaseViewModel by viewModels()
    @Inject
    lateinit var firebaseAuth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAdsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CityNameArray()
        adapter = AllAdapter(requireContext())

        buttons()
        spiners()
    }

    private fun spiners() {
        val spinnerCateg = binding.spinnerCateg
        val spinnerPodCategory = binding.spinnerPodCategory
        val spinnerOldOrNew = binding.spinnerOldOrNew
//        val spinnerCategory = binding.spinner

    }

    private fun buttons() {
        binding.apply {
            bToAddNewAds.setOnClickListener {
                val title = editTitle.text.toString()
                val price = edPrice.text.toString()
                val city = editCity.text.toString()
                val desc = editDesc.text.toString()

                if (title.isEmpty() || price.isEmpty() || city.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(requireContext(), "Заполните все Поля!", Toast.LENGTH_SHORT).show()
                } else {
                    // Получите UID текущего пользователя
                    val user = Firebase.auth.currentUser
                    val uid = user?.uid

                    // Проверьте, что UID пользователя доступен
                    if (uid != null) {

                        val userRef = FirebaseDatabase.getInstance().getReference("users/$uid")

                        // Создайте объект данных для сохранения
                        val data = mapOf(
                            "title" to title,
                            "price" to price,
                            "city" to city,
                            "desc" to desc
                        )

                        // Сохраните данные в базе данных
                        userRef.setValue(data)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Данные успешно сохранены в Firebase!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Ошибка сохранения данных в Firebase: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(requireContext(), "Пользователь не аутентифицирован!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun CityNameArray() {
        val cityAzerbaijan = CityAzerbaijan()
        val cities = cityAzerbaijan.city()

        val autoCompleteCity = binding.editCity
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, cities)
        autoCompleteCity.setAdapter(adapter)
    }

}