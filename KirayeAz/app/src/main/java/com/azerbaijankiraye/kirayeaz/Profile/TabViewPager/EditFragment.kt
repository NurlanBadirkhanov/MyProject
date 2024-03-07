package com.azerbaijankiraye.kirayeaz.Profile.TabViewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.azerbaijankiraye.kirayeaz.databinding.FragmentEditBinding


class EditFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentEditBinding
    private var lastUpdateTime: Long = 0
    private val updateDelayMillis = 30000 // 30 секунд
    lateinit var database: FirebaseDatabase
    private val uid = FirebaseAuth.getInstance().uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance()
        getData()





        binding.button.setOnClickListener {
            setData()
        }

    }


    private fun setData() {
        if (binding.editName.text.isEmpty() || binding.editGmail.text.isEmpty()) {
            Toast.makeText(requireContext(), "Пустые строки недопустимы!", Toast.LENGTH_LONG).show()
        } else {

            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdateTime >= updateDelayMillis) {
                // Ваш код обновления данных
                lastUpdateTime = currentTime
                val user = FirebaseAuth.getInstance().currentUser
                val uid = user?.uid
                val database = FirebaseDatabase.getInstance()

                if (user != null && uid != null) {
                    // Ссылка на базу данных "users / uid"
                    val userRef = database.reference.child("users").child(uid)

                    // Получите текст из EditText
                    val newName = binding.editName.text.toString()
                    val gmail = binding.editGmail.text.toString()

                    val updates = hashMapOf<String, Any>(
                        "name" to newName,
                        "gmail" to gmail,
                    )

                    userRef.updateChildren(updates)
                        .addOnSuccessListener {
                            // Успешное обновление
                            Toast.makeText(
                                requireContext(),
                                "Данные успешно обновлены",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Ошибка при обновлении данных: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Пользователь не аутентифицирован",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Подождите немного перед следующим обновлением",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun getData() {
        val getData = database.reference.child("users").child(uid.toString())

        getData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val auth = FirebaseAuth.getInstance()
//                val gmailValue = snapshot.child("gmail").getValue(String::class.java)
                val getName = snapshot.child("name").getValue(String::class.java)
                val getNumber = snapshot.child("number").getValue(String::class.java)
                val gmail = auth.currentUser!!.email

                binding.editName.setText(getName)
                binding.number.setText(getNumber)
                binding.editGmail.setText(gmail)



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}

