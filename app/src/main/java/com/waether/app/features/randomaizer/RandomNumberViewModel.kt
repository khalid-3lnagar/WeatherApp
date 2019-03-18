package com.waether.app.features.randomaizer

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class RandomNumberViewModel : ViewModel() {
    val randomNumberLiveData = MutableLiveData<Int>()

    init {
        randomNumberLiveData.value = 20
    }
}