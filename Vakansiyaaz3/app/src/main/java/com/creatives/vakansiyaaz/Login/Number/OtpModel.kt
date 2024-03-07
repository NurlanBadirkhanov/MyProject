package com.creatives.vakansiyaaz.Login.Number

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.PhoneAuthProvider

class OtpModel(application: Application) : AndroidViewModel(application) {
    var email = MutableLiveData<String>()
    var OTP = MutableLiveData<String>()
    var phoneNumber = MutableLiveData<String>()

    val resendTokenLiveData: MutableLiveData<PhoneAuthProvider.ForceResendingToken?> by lazy {
        MutableLiveData<PhoneAuthProvider.ForceResendingToken?>()
    }

    fun updateResendToken(resendToken: PhoneAuthProvider.ForceResendingToken?) {
        resendTokenLiveData.value = resendToken
    }
}
