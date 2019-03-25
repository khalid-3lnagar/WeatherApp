package com.weather.app.features.randomaizer

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weather.useecasses.DEFAULT_CLICKED_VALUE
import com.weather.useecasses.changeClickedState
import com.weather.useecasses.numberIncrement

const val DEFAULT_INCREMENTER_VALUE = 0

class RandomNumberViewModel : ViewModel() {

    val numberLiveData by lazy { MutableLiveData<Int>().apply { value = DEFAULT_INCREMENTER_VALUE } }
    val clickedState by lazy { MutableLiveData<String>().apply { value = DEFAULT_CLICKED_VALUE } }


    fun incrementNumber() = numberIncrement(numberLiveData)

    fun changeClickedState() = changeClickedState(clickedState)
}