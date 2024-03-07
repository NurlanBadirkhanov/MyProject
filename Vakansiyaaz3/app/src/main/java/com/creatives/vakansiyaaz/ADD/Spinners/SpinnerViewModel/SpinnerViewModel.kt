package com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpinnerViewModel : ViewModel() {
    val city = MutableLiveData<String?>()
    val time = MutableLiveData<String?>()
    val hegreeEdu = MutableLiveData<String?>()
    val spheraWork = MutableLiveData<String?>()
    val expirence = MutableLiveData<String?>()
    val description = MutableLiveData<String?>()

    fun resetValues() {
        city.value = null
        time.value = null
        hegreeEdu.value = null
        spheraWork.value = null
        expirence.value = null
        description.value = null
    }



}