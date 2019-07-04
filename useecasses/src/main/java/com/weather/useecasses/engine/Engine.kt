package com.weather.useecasses.engine

import android.arch.lifecycle.MutableLiveData

fun <T> T.toMutableLiveData(): MutableLiveData<T> = MutableLiveData<T>().also { it.postValue(this) }



