package com.azerbaijankiraye.kirayeaz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.azerbaijankiraye.kirayeaz.databinding.ActivityWeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WeActivity : AppCompatActivity() {
    private var originalStatusBarColor: Int = 0
    lateinit var binding: ActivityWeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onStop() {
        super.onStop()
        window.statusBarColor = originalStatusBarColor
    }

    override fun onBackPressed() {
        super.onBackPressed()
        window.statusBarColor = originalStatusBarColor
    }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
    }

    override fun onResume() {
        super.onResume()
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        setContentView(binding.root)
        buttons()
        binding.bBack.setOnClickListener {
            finish()
        }
    }

    private fun buttons() {
        binding.apply {
            bTiktok?.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!checkTiktokBonusIssued()) {
                        openTikTok()
                        delay(10000)
                        updateTikTokBalance(true)
                        markTikTokBonusIssued()
                    } else {
                        openTikTok()
                        Toast.makeText(this@WeActivity, "Instagram бонус уже выдан", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            bInstag?.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!checkInstagramBonusIssued()) {
                        openInstagram()
                        delay(10000)
                        updateInstagramBalance(true)
                        markInstagramBonusIssued()
                    } else {
                        openInstagram()
                        Toast.makeText(this@WeActivity, "Instagram бонус уже выдан", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            bWhatsap?.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!checkWhatsappBonusIssued()) {
                        openWhatsApp()
                        delay(10000)
                        updateWhatsappBalance(true)
                        markWhatsappBonusIssued()
                    } else {
                        openWhatsApp()
                        Toast.makeText(this@WeActivity, "WhatsApp бонус уже выдан", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateInstagramBalance(increase: Boolean) {
        val userId = firebaseAuth.currentUser?.uid
        val userBalanceRef = userId?.let { databaseReference.child("users")
            .child(it)
            .child("balance") }

        userBalanceRef?.get()?.addOnSuccessListener { snapshot ->
            val currentBalance = snapshot.value as? Long ?: 0

            val newBalance = if (increase) currentBalance + 1 else currentBalance
            userBalanceRef.setValue(newBalance)
        }?.addOnFailureListener {
            Toast.makeText(this, "Ошибка при обновлении Instagram баланса", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateTikTokBalance(increase: Boolean) {
        val userId = firebaseAuth.currentUser?.uid
        val userBalanceRef = userId?.let { databaseReference.child("users")
            .child(it)
            .child("balance") }

        userBalanceRef?.get()?.addOnSuccessListener { snapshot ->
            val currentBalance = snapshot.value as? Long ?: 0

            val newBalance = if (increase) currentBalance + 1 else currentBalance
            userBalanceRef.setValue(newBalance)
        }?.addOnFailureListener {
            Toast.makeText(this, "Ошибка при обновлении TikTok баланса", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWhatsappBalance(increase: Boolean) {
        val userId = firebaseAuth.currentUser?.uid
        val userBalanceRef = userId?.let { databaseReference.child("users")
            .child(it)
            .child("balance") }

        userBalanceRef?.get()?.addOnSuccessListener { snapshot ->
            val currentBalance = snapshot.value as? Long ?: 0

            val newBalance = if (increase) currentBalance + 1 else currentBalance
            userBalanceRef.setValue(newBalance)
        }?.addOnFailureListener {
            Toast.makeText(this, "Ошибка при обновлении WhatsApp баланса", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun checkInstagramBonusIssued(): Boolean {
        val userId = firebaseAuth.currentUser?.uid
        val bonusIssuedRef = userId?.let { databaseReference.child("users")
            .child(it)
            .child("instagramBonusIssued") }

        return bonusIssuedRef?.get()?.await()?.value as? Boolean ?: false
    }
    private suspend fun checkTiktokBonusIssued(): Boolean {
        val userId = firebaseAuth.currentUser?.uid
        val bonusIssuedRef = userId?.let { databaseReference.child("users")
            .child(it)
            .child("tikTokBonusIssued") }

        return bonusIssuedRef?.get()?.await()?.value as? Boolean ?: false
    }

    private suspend fun checkWhatsappBonusIssued(): Boolean {
        val userId = firebaseAuth.currentUser?.uid
        val bonusIssuedRef = userId?.let { databaseReference.child("users")
            .child(it)
            .child("whatsappBonusIssued") }

        return bonusIssuedRef?.get()?.await()?.value as? Boolean ?: false
    }

    private fun markInstagramBonusIssued() {
        val userId = firebaseAuth.currentUser?.uid
        val bonusIssuedRef = userId?.let { databaseReference.child("users").child(it).child("instagramBonusIssued") }

        bonusIssuedRef?.setValue(true)
    }
    private fun markTikTokBonusIssued() {
        val userId = firebaseAuth.currentUser?.uid
        val bonusIssuedRef = userId?.let { databaseReference
            .child("users")
            .child(it)
            .child("tikTokBonusIssued") }

        bonusIssuedRef?.setValue(true)
    }

    private fun markWhatsappBonusIssued() {
        val userId = firebaseAuth.currentUser?.uid
        val bonusIssuedRef = userId?.let { databaseReference.child("users").child(it).child("whatsappBonusIssued") }

        bonusIssuedRef?.setValue(true)
    }

    private fun openInstagram() {
        val uri = Uri.parse("https://www.instagram.com/kiraya.az/?hl=ru")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }
    private fun openTikTok() {
        val uri = Uri.parse("https://www.tiktok.com/@kiraye_az")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }

    private fun openWhatsApp() {
        val uri = Uri.parse("https://www.whatsapp.com/channel/0029VaHGclk9MF90XA4YF30n")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }
}
