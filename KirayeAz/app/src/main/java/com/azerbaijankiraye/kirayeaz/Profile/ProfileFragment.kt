package com.azerbaijankiraye.kirayeaz.Profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.azerbaijankiraye.kirayeaz.Profile.Firebase.FirebaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kaopiz.kprogresshud.KProgressHUD
import com.azerbaijankiraye.kirayeaz.Profile.TabViewPager.MyAdsFragment
import com.azerbaijankiraye.kirayeaz.Profile.TabViewPager.EditFragment
import com.azerbaijankiraye.kirayeaz.Profile.Firebase.GetDataUsers
import com.azerbaijankiraye.kirayeaz.Profile.ItemProfile.CategoryAddAdsFragment
import com.azerbaijankiraye.kirayeaz.databinding.FragmentProfileBinding
import kotlinx.coroutines.*
import com.azerbaijankiraye.kirayeaz.R
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("users")
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userUid = currentUser?.uid
    private var backPressedOnce = false

    var hud: KProgressHUD? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        showProgressBar(requireContext())
        showWaitingDialogIfNeeded()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedOnce) {
                requireActivity().finish()
            } else {
                backPressedOnce = true
                Toast.makeText(requireContext(), "Ещё раз для выхода", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ backPressedOnce = false }, 2000) // Сброс флага через 2 секунды
            }
        }
        callback.isEnabled = true

        if (auth.currentUser == null) {
            startActivity(Intent(requireContext(), FirebaseActivity::class.java))
            activity!!.finish()
        } else {
            lifecycleScope.launch {
                getDataFromFirebase()
            }
            buttons()


        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            hud?.dismiss()
        }
    }

    private fun showProgressBar(context: Context) {
        hud = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Пожалуйста, подождите")
            .setCancellable(true)
            .setAnimationSpeed(2)

        hud?.show()
    }


    private suspend fun getDataFromFirebase() {
        withContext(Dispatchers.IO) {
            if (auth.currentUser != null) {
                val phoneNumber = auth.currentUser!!.phoneNumber

                withContext(Dispatchers.Main) {
                    binding.tvNumber.text = phoneNumber
                }

                if (userUid != null) {
                    val userListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val user = dataSnapshot.getValue(GetDataUsers::class.java)

                                if (user != null) {
                                    val balance = user.balance
                                    val name = user.name
                                    val number = user.number


                                    binding.tvBalance.text = "$balance AZN"
                                    binding.tvName.text = name
                                    binding.tvNumber.setText(number)


                                } else {
                                    // Обработка случая, когда данные пользователя не найдены
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Обработка ошибок при чтении данных
                        }
                    }

                    usersRef.child(userUid).addListenerForSingleValueEvent(userListener)
                } else {
                    // Обработка случая, когда UID пользователя недоступен
                }
            } else {
                withContext(Dispatchers.Main) {
                    startActivity(Intent(requireContext(), FirebaseActivity::class.java))
                }
            }
        }
    }


    private fun buttons() {
        binding.apply {
            buttonMy.setOnClickListener {
                showProgressBar(requireContext())
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    val myAdsFragment = MyAdsFragment()
                    myAdsFragment.show(parentFragmentManager, myAdsFragment.tag)
                    hud?.dismiss()
                }
            }

            bNewAds.setOnClickListener {
                val bottomSheetFragment = CategoryAddAdsFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
//                findNavController().navigate(R.id.action_action_profile_to_newAdsFragment)

            }
            bSettings.setOnClickListener {
                findNavController().navigate(R.id.action_action_profile_to_settingsFragment)
            }
            bToAddBalance.setOnClickListener {
               findNavController().navigate(R.id.action_action_profile_to_balanceFragment)
            }
            bToEdit.setOnClickListener {
                showProgressBar(requireContext())
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    val bottomSheetFragment = EditFragment()
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                    hud?.dismiss()
                }
            }
            bToHistory.setOnClickListener {
                findNavController().navigate(R.id.action_action_profile_to_historyDetailFragment)
            }

        }
    }
    private fun showWaitingDialogIfNeeded() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val database = FirebaseDatabase.getInstance().reference
            val usersRef = database.child("users")
            val userRef = usersRef.child(uid)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val nameExists = dataSnapshot.child("name").exists()
                    val numberExists = dataSnapshot.child("number").exists()

                    if (!nameExists || !numberExists) {
                        // Отобразить диалог с ожиданием и формой ввода
                        showWaitingDialog()
                    } else {
                        hud!!.dismiss()

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибок при чтении данных из базы данных
                }
            })
        }
    }

    private fun showWaitingDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog, null)
        val progressBar: ProgressBar = dialogView.findViewById(R.id.progressBar)
        val editTextName: EditText = dialogView.findViewById(R.id.editTextName)
        val editTextNumber: EditText = dialogView.findViewById(R.id.editTextNumber)
        progressBar.visibility = View.GONE

        val alertDialog = MaterialAlertDialogBuilder(requireContext(),R.drawable.back3)
            .setView(dialogView)
            .setTitle("Ожидание")
            .setCancelable(false)
            .create()

        // Добавьте обработчик для кнопки ОК
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, _ ->
            progressBar.visibility = View.VISIBLE
            val name = editTextName.text.toString().trim()
            val number = editTextNumber.text.toString().trim()

            if (name.isNotEmpty() && number.isNotEmpty()) {
                // Данные введены корректно, продолжайте выполнение
                // Пример сохранения данных в SharedPreferences:
                val sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("name", name)
                editor.putString("number", number)
                editor.apply()
                dialog.dismiss()
                setNameOrNumberToFirebase(name, number)

                hud!!.dismiss()

            } else {
                hud!!.dismiss()
                Toast.makeText(requireContext(), "Пожалуйста, введите имя и номер", Toast.LENGTH_SHORT).show()
                hud!!.dismiss()
                showWaitingDialogIfNeeded()

            }
        }

        alertDialog.show()
    }




    private fun setNameOrNumberToFirebase(name: String, number: String) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val database = FirebaseDatabase.getInstance().reference
            val usersRef = database.child("users")
            val userRef = usersRef.child(uid)

            if (name.isNotEmpty()) {
                userRef.child("name").setValue(name)
            }

            if (number.isNotEmpty()) {
                userRef.child("number").setValue(number)
            }
        }
        val user = FirebaseAuth.getInstance().currentUser
        val uids = user?.uid
        val database = FirebaseDatabase.getInstance()
        if (user != null && uids != null) {
            val userRef = database.reference.child("users").child(uids)


            val updates = hashMapOf<String, Any>(
                "name" to name,
                "number" to number,
            )

            userRef.updateChildren(updates)
                .addOnSuccessListener {
                    // Успешное обновление
                    Toast.makeText(
                        requireContext(),
                        "Данные успешно Сохранены",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Ошибка при cохранении данных: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

}

