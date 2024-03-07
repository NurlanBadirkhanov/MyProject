package com.creatives.vakansiyaaz.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.MainActivity
import com.creatives.vakansiyaaz.Login.Number.NumberActivity
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.R.color.signin
import com.creatives.vakansiyaaz.databinding.FragmentFirstBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class SignInFragment : Fragment() {

    lateinit var binding: FragmentFirstBinding
    private var originalStatusBarColor: Int = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), signin)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), signin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initGoogle()
        button()

    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("324862035176-nvnsfh32pmo8b4jai8mb7vhkvidtr3ah.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }


    private fun signWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("MyLog", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("MyLog", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MyLog", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("MyLog", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra(EXTRA_NAME, user.displayName)
            startActivity(intent)
        }
    }

    companion object {
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME = "EXTRA_NAME"
    }

    private fun button() {
        binding.apply {
            bToEmailPass.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_emailPassFragment)
            }
            bToGoogle.setOnClickListener {
                signWithGoogle()

            }
            bToNumberA.setOnClickListener {
                startActivity(Intent(requireContext(), NumberActivity::class.java))
                activity!!.finish()

            }

            bToAnon.setOnClickListener {
                startActivity(Intent(requireContext(),MainActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity!!.window.statusBarColor = originalStatusBarColor
    }
}