package com.weather.useecasses

import android.arch.lifecycle.MutableLiveData


fun numberIncrement(liveData: MutableLiveData<Int>, incrementValue: Int = 1) =
    liveData.postValue((liveData.value ?: 0) + incrementValue)






