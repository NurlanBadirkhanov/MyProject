package com.azerbaijankiraye.kirayeaz.Profile.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentAdminRegisterBinding


class AdminRegisterFragment : Fragment() {
    lateinit var binding: FragmentAdminRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            button2.setOnClickListener {
                val login = editTextTextEmailAddress.text.toString()
                val pass = editTextTextPassword.text.toString()

                if (login.isNotEmpty() && pass.isNotEmpty()) {
                    // Подключение к Firebase Authentication
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(login, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Аутентификация успешна
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                val isAdmin = currentUser?.email == "kirayeazerbaijan38@gmail.com"

                                if (isAdmin) {
                                    // Только для админа
                                    findNavController().navigate(R.id.action_adminRegisterFragment_to_adminPanelFragment)
                                } else {
                                    activity!!.finishAffinity()
                                    activity!!.finish()
                                    Toast.makeText(requireContext(), "Ты не я, я не ты, Лучше уйди!", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                activity!!.finishAffinity()
                                activity!!.finish()
                                Toast.makeText(requireContext(), "Ты не я, я не ты, Лучше уйди!", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    // Обработка ситуации, когда login или pass пусты
                    Toast.makeText(requireContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}




