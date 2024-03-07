package com.creatives.vakansiyaaz.Login.Number

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.creatives.vakansiyaaz.MainActivity
import com.creatives.vakansiyaaz.databinding.FragmentNumberCodeBinding
import com.goodiebag.pinview.Pinview
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit


class NumberCodeFragment : Fragment() {
    private lateinit var binding: FragmentNumberCodeBinding
    private lateinit var model: OtpModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(OtpModel::class.java)
        initFirebase()
        Log.d("MyLog", " Resend token value: ${model.resendTokenLiveData.value.toString()}")
        Log.d("MyLog", " phoneNumber  value: ${model.phoneNumber.value}")


        binding.pinview.setPinViewEventListener(object : Pinview.PinViewEventListener {
            override fun onDataEntered(pinview: Pinview?, fromUser: Boolean) {
                val enteredValue = pinview?.value ?: ""

                Log.d("MyLog", "Pin entered: $enteredValue")
                resendVerificationCode()

            }
        })
        binding.bOk.setOnClickListener {
            Log.d("MyLog", "Resending verification code button clicked.")
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                model.OTP.value.toString(), model.resendTokenLiveData.value.toString()
            )
            signInWithPhoneAuthCredential(credential)
        }


    }



    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        Log.d("MyLog", "Initialized Firebase.")
    }
    private fun resendVerificationCode() {
        val optionsBuilder = model.resendTokenLiveData.value?.let { token ->
           PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(model.phoneNumber.value.toString())       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
            .build()
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder!!)

    }



    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("MyLog", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("MyLog", "onVerificationFailed: ${e.toString()}")
            }
            // Show a message and update the UI
        }
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d("MyLog", "onCodeSent: Verification ID: $verificationId, Token: $token")
            model.OTP.value = verificationId
            model.resendTokenLiveData.value = token
        }

    }
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(requireContext(), "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    private fun sendToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }
}
