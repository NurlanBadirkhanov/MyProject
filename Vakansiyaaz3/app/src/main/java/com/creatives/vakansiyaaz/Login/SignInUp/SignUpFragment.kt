package com.creatives.vakansiyaaz.Login.SignInUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.Login.Number.OtpModel
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var model: OtpModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        model = ViewModelProvider(requireActivity()).get(OtpModel::class.java)
        buttons()
    }

    private fun buttons() {
        binding.bToSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.bSignUp.setOnClickListener {
            val email = binding.editLogin.text.toString()
            val password = binding.editPass.text.toString()
            val confirmPassword = binding.editConfirnPass.text.toString()
            if (email.isNotEmpty() || password.isNotEmpty() || confirmPassword.isNotEmpty()) {
                signUpWithEmail(email, password, confirmPassword)
            } else {
                Toast.makeText(
                    requireContext(), "Registration failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signUpWithEmail(email: String, password: String, confirmPassword: String) {
        if (password == confirmPassword) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // После успешной регистрации отправляем письмо с подтверждением
                        sendEmailVerification(email, password)
                    } else {
                        // Регистрация не удалась
                        Toast.makeText(
                            requireContext(), "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // Пароли не совпадают
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmailVerification(email: String, password: String) {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Письмо с подтверждением успешно отправлено
                    Toast.makeText(
                        requireContext(), "Verification email sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    setDataFirebase()
                    findNavController().navigate(R.id.action_SecondFragment_to_verficationFragment)
                } else {
                    // Ошибка отправки письма
                    Toast.makeText(
                        requireContext(), "Failed to send verification email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setDataFirebase() {
        val user = auth.currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val gmail = binding.editLogin.text.toString()
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val database = firebaseDatabase.reference
            val usersRef = database.child("users")
            val userRef = usersRef.child(uid)

            val registrationDate = System.currentTimeMillis()
            val userRefData = usersRef.child(uid)
            userRefData.child("registrationDate").setValue(registrationDate)
            userRefData.child("gmail").setValue(gmail)

            userRef.child("balance").setValue(0)
            userRef.child("verification").setValue(false)
        } ?: run {
            Toast.makeText(requireContext(), "Пользователь не аутентифицирован", Toast.LENGTH_SHORT).show()
        }
    }
}
