
package com.waether.app.features.randomaizer

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weather.useecasses.Ticker
import com.weather.useecasses.randomNumberGenerator
import com.weather.useecasses.rxTicker

const val DEFAULT_RANDOM_VALUE = 0

fun MutableLiveData<Int>.postRandomValue() = postValue(randomNumberGenerator())
class RandomNumberViewModel : ViewModel() {
    val randomNumberLiveData = MutableLiveData<Int>()
    val secondRandomNumber = MutableLiveData<Int>()

    private val timer = Ticker { randomNumberLiveData.postRandomValue() }
    private val rxTimer = rxTicker { randomNumberLiveData.postRandomValue() }

    init {
        randomNumberLiveData.value = DEFAULT_RANDOM_VALUE
        secondRandomNumber.value = DEFAULT_RANDOM_VALUE
        timer.start()
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        rxTimer.dispose()
    }
}