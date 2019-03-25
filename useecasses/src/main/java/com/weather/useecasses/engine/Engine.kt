package com.weather.useecasses.engine

import android.arch.lifecycle.MutableLiveData

fun <T> T.toMutableLiveData(): MutableLiveData<T> = MutableLiveData<T>().also { it.postValue(this) }

fun <T> MutableList<T>.listIf(block: (field: T) -> Boolean): MutableList<T> {
    val list = mutableListOf<T>()
    forEach { if (block(it)) list.add(it) }
    return list

}



