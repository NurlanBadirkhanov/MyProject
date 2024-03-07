package com.azerbaijankiraye.kirayeaz.Profile.Firebase

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azerbaijankiraye.kirayeaz.MainActivity
import com.azerbaijankiraye.kirayeaz.Profile.Admin.AdminActivity
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentSignINBinding
import com.google.firebase.auth.FirebaseAuth

class SignINFragment : Fragment() {

    lateinit var binding: FragmentSignINBinding
    lateinit var auth: FirebaseAuth
    private var originalStatusBarDrawable: Drawable? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignINBinding.inflate(layoutInflater, container, false)
        activity?.window?.setBackgroundDrawableResource(R.drawable.singback)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFirebase()
        checkAuthState()
        button()


    }

    private fun button() {
        val passwordEditText = binding.pass
        val showPasswordCheckBox = binding.showPasswordCheckBox

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Показать пароль
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                // Скрыть пароль
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Установить курсор в конец текста
            passwordEditText.setSelection(passwordEditText.text!!.length)
        }

        binding.bToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signINFragment_to_signUpFragment)
        }
        binding.signIN.setOnClickListener {
            signFirebase()
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun signFirebase() {
        val email = binding.email.text.toString()
        val pass = binding.pass.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            // Пользователь успешно аутентифицирован
                            if (isAdminUser(user.email)) {
                                startActivity(Intent(requireContext(), AdminActivity::class.java))
                            } else {
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                            }
                            Toast.makeText(
                                requireContext(),
                                "Успешная Авторизация!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Обработка ситуации, когда текущий пользователь не получен
                            Toast.makeText(
                                requireContext(),
                                "Ошибка при получении информации о пользователе",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        // Обработка ошибок аутентификации
                        Toast.makeText(
                            requireContext(),
                            "Ошибка при авторизации: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Пустые поля не допускаются!!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isAdminUser(email: String?): Boolean {
        // Проверка, является ли пользователь администратором на основе email
        return email == "994773022307@gmail.com"
    }


    private fun checkAuthState() {
        if (auth.currentUser != null) {
            val i = Intent(requireContext(), MainActivity::class.java)
            startActivity(i)
        }
    }
}



