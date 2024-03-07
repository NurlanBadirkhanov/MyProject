package com.creatives.vakansiyaaz.ADD

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.ADD.Spinners.DescriptionFragment
import com.creatives.vakansiyaaz.ADD.Spinners.ExpirienceWorkFragment
import com.creatives.vakansiyaaz.ADD.Spinners.HegreeEducationFragment
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.ADD.Spinners.TimeWorkFragment
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentAddVacanyBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class AddVacancyFragment : Fragment() {

    private lateinit var binding: FragmentAddVacanyBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var viewModel: SpinnerViewModel
    var verification by Delegates.notNull<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVacanyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        initFirebase()
        buttons()
        setupObserver()
        bOk()
        bMaterialDialog()


    }
    private fun bMaterialDialog() {
        binding.bIm.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setTitle("О Преимуществах")
            dialog.setMessage("1) Вакансия длится 2 дня.")
                .setPositiveButton("Ок") { _, _ ->

                }.show()
        }
    }

    private fun bOk() {

        val databaseReference = FirebaseDatabase.getInstance().reference
        val userReference = databaseReference.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("verification")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                verification = dataSnapshot.getValue(Boolean::class.java) == true
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        binding.bExit.setOnClickListener {
            findNavController().popBackStack()

            binding.apply {
                viewModel.spheraWork.value = "Сфера"
                viewModel.expirence.value = "Опыт работы"
                viewModel.city.value = "Город"
                viewModel.hegreeEdu.value = "Образование"
                viewModel.time.value = "График работы"
                viewModel.description.value = "Напишите действия"
            }
        }

        binding.bSave.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                val spheraWork = viewModel.spheraWork.value.toString()
                val hegreeEdu = viewModel.hegreeEdu.value.toString()
                val time = viewModel.time.value.toString()
                val expirence = viewModel.expirence.value.toString()
                val city = viewModel.city.value.toString()
                val description = viewModel.description.value.toString()
                val name = binding.editName.text.toString()
                val price = binding.edPrice.text.toString()
                val gmail = binding.editGmail.text.toString()
                val number = binding.editNumber.text.toString()
                val title = binding.edTitle.text.toString()


                if (spheraWork.isNotEmpty() && time.isNotEmpty() && expirence.isNotEmpty() && hegreeEdu.isNotEmpty()
                    && city.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty() && name.isNotEmpty()
                    && gmail.isNotEmpty() && number.isNotEmpty() && title.isNotEmpty()) {
                    saveToDatabase(
                        spheraWork,
                        time,
                        expirence,
                        hegreeEdu,
                        city,
                        price,
                        description,
                        name,
                        gmail,
                        number,
                        verification,
                        title
                    )
                } else {
                     Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }


    private fun setupObserver() {
        viewModel.expirence.observe(viewLifecycleOwner) { value ->
            binding.tvWorkExpir.text = value
        }
        viewModel.hegreeEdu.observe(viewLifecycleOwner) { value ->
            binding.tvEdu.text = value
        }
        viewModel.time.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }
        viewModel.spheraWork.observe(viewLifecycleOwner) { data ->
            binding.tvSfera.text = data
        }
        viewModel.city.observe(viewLifecycleOwner) {
            binding.tvCity.text = it
        }
        viewModel.description.observe(viewLifecycleOwner) {
            binding.edDeck.text = it
        }
    }


    private fun buttons() {
        binding.apply {
            bWorkExpir.setOnClickListener {
                val bottomSheetDialogFragment = ExpirienceWorkFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            bTimeWork.setOnClickListener {
                val bottomSheetDialogFragment = TimeWorkFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            bEdu.setOnClickListener {
                val bottomSheetDialogFragment = HegreeEducationFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            desc.setOnClickListener {
                val bottomSheetDialogFragment = DescriptionFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            bSfera.setOnClickListener {
                findNavController().navigate(R.id.action_addVacancyFragment_to_spheraWorkFragment)
            }
            bCity.setOnClickListener {
                findNavController().navigate(R.id.action_addVacancyFragment_to_cityFragment)
            }

        }


    }

    private suspend fun saveToDatabase(
        Sphera: String,
        TimeWork: String,
        WorkExpirience: String,
        Education: String,
        City: String,
        Price: String,
        Description: String,
        Name: String,
        Gmail: String,
        Number: String,
        verification:Boolean,
        title:String
        ) {



        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)

            val twoDaysInMillis: Long = 2 * 24 * 60 * 60 * 1000L


            val todayData = ServerValue.TIMESTAMP
            val uniqueId = auth.currentUser!!.uid
            val notPro = "notPro"
            val idItem = (10000..99999).random()

            val product = hashMapOf(
                "idElement" to idItem,
                "proAndLink" to notPro,
                "title" to title,
                "uniqueId" to uniqueId,
                "sphera" to Sphera,
                "timeWork" to TimeWork,
                "experience" to WorkExpirience,
                "education" to Education,
                "city" to City,
                "price" to Price,
                "name" to Name,
                "desc" to Description,
                "Gmail" to Gmail,
                "number" to Number,
                "data" to todayData,
                "verification" to verification,
                "twoDaysInMillis" to twoDaysInMillis,

            )


            val sharedId = userRef.child("myAds").push().key
            val newAdRef = product + ("id" to sharedId)

            if (sharedId != null) {
                database.reference
                    .child("Vakancies")
                    .child("Vakancies")
                    .child(sharedId)
                    .setValue(newAdRef)
            }

            if (sharedId != null) {
                userRef.child("myAds").child(sharedId).setValue(newAdRef)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Данные успешно сохранены!",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка сохранения данных",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Пользователь не аутентифицирован!",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }


    }
}
