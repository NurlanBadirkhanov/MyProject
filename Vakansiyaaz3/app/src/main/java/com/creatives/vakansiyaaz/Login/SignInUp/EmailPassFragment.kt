package com.creatives.vakansiyaaz.Login.SignInUp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.MainActivity
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentEmailPassBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth


class EmailPassFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    lateinit var binding: FragmentEmailPassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentEmailPassBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        buttons()
    }

    private fun buttons() {
        binding.apply {
            bSignIn.setOnClickListener {
                signInToFirebase()
            }
            bResetPass.setOnClickListener {
                showForgotPasswordDialog()
            }
            bSignUp2.setOnClickListener {
                findNavController().navigate(R.id.action_emailPassFragment_to_SecondFragment)
            }
        }
    }

    private fun signInToFirebase() {
        val login = binding.editLogin.text.toString()
        val password = binding.editPass.text.toString()

        if (login.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity!!.finish()
                    } else {

                        Toast.makeText(
                            requireContext(), "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Please enter both login and password", Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Письмо успешно отправлено, дополнительные действия, например, переход на другой экран
                    // ...
                } else {
                    // Ошибка отправки письма, выводим сообщение об ошибке
                    Toast.makeText(
                        requireContext(), "Email verification failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun signUpWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Регистрация успешна, отправляем письмо с кодом подтверждения
                    sendEmailVerification()
                } else {
                    // Регистрация не удалась, выводим сообщение об ошибке
                    Toast.makeText(
                        requireContext(), "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun showForgotPasswordDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_forgot_password, null)

        val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)

        builder.setView(dialogView)
            .setTitle("Сброс пароля")
            .setPositiveButton("Отправить") { _, _ ->

                val email = emailEditText.text.toString()

                if (email.isNotEmpty()) {
                    // Запрос на сброс пароля
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                showSuccessDialog()
                            } else {
                                // Ошибка при отправке письма
                                // Можно обработать ошибку
                                showErrorDialog()
                            }
                        }
                } else {
                    // Электронная почта не введена
                    // Можно предупредить пользователя
                    showErrorDialog()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


    private fun showSuccessDialog() {
        Toast.makeText(requireContext(), "Письмо для сброса пароля успешно отправлено", Toast.LENGTH_SHORT).show()
    }

    private fun showErrorDialog() {
        Toast.makeText(requireContext(), "Ошибка при сбросе пароля. Пожалуйста, проверьте введенную электронную почту", Toast.LENGTH_SHORT).show()
    }



}