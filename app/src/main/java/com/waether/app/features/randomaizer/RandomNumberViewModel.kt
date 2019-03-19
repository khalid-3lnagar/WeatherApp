
package com.waether.app.features.randomaizer

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weather.useecasses.numberIncrement

const val DEFAULT_RANDOM_VALUE = 0

class RandomNumberViewModel : ViewModel() {

    val randomNumberLiveData = MutableLiveData<Int>()
    init {
        randomNumberLiveData.value = DEFAULT_RANDOM_VALUE
    }

    fun incrementNumber() = numberIncrement(randomNumberLiveData)

}