package com.creatives.vakansiyaaz.Login.Number

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.MainActivity
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentNumberSignBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit


class NumberSignFragment : Fragment() {
    private lateinit var binding:FragmentNumberSignBinding
    private lateinit var model: OtpModel
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var phoneNumberET : EditText
    lateinit var number:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberSignBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(OtpModel::class.java)
        initFirebase()
        buttons()


    }

    private fun buttons() {
        binding.bOk.setOnClickListener {
            number =  binding.edNumber.text.toString()
            model.phoneNumber.value = number
            Log.d("MyLog", "Phone number: $number") // Добавленный лог
            if (number.isNotEmpty()) {
                sendVerificationCode(number)
            } else {
                Toast.makeText(requireContext(), "Введите номер телефона", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        phoneNumberET = binding.edNumber


    }


    private fun sendVerificationCode(phoneNumber: String) {
        Log.d("MyLog", "Sending verification code to: $phoneNumber") // Добавленный лог
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun sendToMain() {
        Log.d("MyLog", "Redirecting to MainActivity") // Добавленный лог
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext() , "Authenticate Successfully" , Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("MyLog", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                binding.mProgressBar.visibility = View.INVISIBLE
            }
    }

//    private fun sendToMain(){
//        startActivity(Intent(requireContext() , MainActivity::class.java))
//    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        Log.d("MyLog", "Verification completed. Credential: $credential") // Добавленный лог
        signInWithPhoneAuthCredential(credential)
    }


    override fun onVerificationFailed(e: FirebaseException) {
        Log.d("MyLog", "Verification failed. Exception: $e") // Добавленный лог

        if (e is FirebaseAuthInvalidCredentialsException) {
            Log.d("MyLog", "Invalid credentials") // Добавленный лог
        } else if (e is FirebaseTooManyRequestsException) {
            Log.d("MyLog", "Too many requests") // Добавленный лог
        } else {
            // Дополнительная обработка других ошибок
            Log.d("MyLog", "Other error: ${e.message}")
        }

        binding.mProgressBar.visibility = View.VISIBLE
    }



    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        Log.d("MyLog", "Code sent. VerificationId: $verificationId, Token: $token") // Добавленный лог
        model.OTP.value = verificationId
        model.updateResendToken(token)
        model.phoneNumber.value = number
        binding.mProgressBar.visibility = View.INVISIBLE
        Log.d("MyLog", "number: ${model.phoneNumber.value}.")
        Log.d("MyLog", "token: $token}.")
        Log.d("MyLog", "otp: ${model.OTP.value}.")
        Log.d("MyLog", "resendTokenLiveData: ${model.resendTokenLiveData.value}.")

        findNavController().navigate(R.id.action_numberSignFragment_to_numberCodeFragment)
    }
    }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(requireContext() , MainActivity::class.java))
        }
    }
}