package com.weather.useecasses

import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.useecasses.reposotereys.CitiesRepository

const val DEFAULT_CLICKED_VALUE = "un clicked"
const val ON_CLICK_CLICKED_VALUE = "clicked"

fun numberIncrement(liveData: MutableLiveData<Int>, incrementValue: Int = 1) =
    liveData.postValue((liveData.value ?: 0) + incrementValue)

fun changeClickedState(liveData: MutableLiveData<String>) {
    if (liveData.value.equals(DEFAULT_CLICKED_VALUE))
        liveData.postValue(ON_CLICK_CLICKED_VALUE)
    else
        liveData.postValue(DEFAULT_CLICKED_VALUE)

}


// usecase 1 : search city by name
// if is searching, then do not trigger action
// city name must not be null
// if all is OK, trigger search
fun retrieveCityByName(
    cityName: String?,
    retrieving: MutableLiveData<Boolean?>,
    repository: CitiesRepository?,
    result: MutableLiveData<List<City>>
) {
    cityName
        ?.takeUnless { retrieving.value ?: false }
        ?.takeUnless { it.isBlank() }
        ?.also { retrieving.postValue(true) }
        ?.let { repository?.searchCitiesByName(it) ?: listOf() }
        ?.also(result::postValue)
        ?.also { retrieving.value = false }

}

// usecase 2 : retrieve favorite cities ids (longs)
// if is retrieving, then do not trigger action
// if favorites is empty, throw an exception
// if favorites not empty, convert them to ids (longs)

fun retrieveFavoriteCitiesByIds(
    retrieving: MutableLiveData<Boolean?>,
    repository: CitiesRepository?
): List<Long> {
    return retrieving
        .takeUnless { it.value ?: false }
        ?.also { it.postValue(true) }
        ?.let { repository?.retrieveFavoritesCitiesIds()!! }
        ?.map { it.id }
        ?.also { retrieving.postValue(false) }!!

}

// usecase 3 : retrieve cities by Ids
// if is retrieving, then do not trigger action
// if all is Ok, trigger action
fun retrieveCitiesByIds(
    ids: List<Long>,
    retrieving: MutableLiveData<Boolean?>,
    repository: CitiesRepository?,
    result: MutableLiveData<List<City>>
) {
    ids.takeUnless { retrieving.value ?: false && ids.isEmpty() }
        ?.also { retrieving.postValue(true) }
        ?.let { repository?.retrieveCitiesIds(it)!! }
        ?.also { result.postValue(it) }
        ?.also { retrieving.postValue(false) }
}




