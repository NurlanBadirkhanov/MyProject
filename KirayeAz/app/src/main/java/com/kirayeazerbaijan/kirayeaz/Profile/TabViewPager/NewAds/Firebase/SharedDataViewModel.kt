package com.kirayeazerbaijan.kirayeaz.Profile.TabViewPager.NewAds.Firebase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedDataViewModel : ViewModel() {
    val storedVerificationId = MutableLiveData<String>()
}
