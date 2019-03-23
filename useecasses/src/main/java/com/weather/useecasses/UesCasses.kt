package com.weather.useecasses

import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.useecasses.reposotereys.CitiesRepository
import com.weather.useecasses.reposotereys.CitiesRepositoryBase

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

fun retrieveCityByName(
    cityName: String?,
    retrieving: MutableLiveData<Boolean>,
    repository: CitiesRepositoryBase,
    result: MutableLiveData<List<City>>
) {
    cityName
        ?.takeUnless { retrieving.value ?: false }
        ?.takeUnless { it.isBlank() }
        ?.also { retrieving.postValue(true) }
        ?.let { repository.retrieveCitiesByName(it) }
        ?.also(result::postValue)
        ?.also { retrieving.value = false }
/*searchName
        ?.takeUnless { retrieving.value ?: false }
        ?.takeUnless { it.isBlank() }
        ?.also { retrieving.postValue(true) }
        ?.let { repository.searchCitiesByName(it) }
        ?.also { result.postValue(it) }
        ?.also { retrieving.postValue(false) }
*/
}
fun retrieveFavoriteCitiesIds(
    retrieving: MutableLiveData<Boolean>,
    repository: CitiesRepositoryBase
): List<Long>? {
    return retrieving
        .takeUnless { it.value ?: false }
        ?.also { it.postValue(true) }
        ?.let { repository.retrieveFavoritesCitiesIds() }
        ?.ifEmpty { throw Exception() }
        ?.map { it.id }
        ?.also { retrieving.postValue(false) }

}

fun retrieveCitiesByIds(
    ids: List<Long>,
    retrieving: MutableLiveData<Boolean?>,
    repository: CitiesRepository?,
    result: MutableLiveData<List<City>>
) {
    ids.takeUnless { retrieving.value ?: false && ids.isEmpty() }
        ?.also { retrieving.postValue(true) }
        ?.let { repository?.retrieveCitiesByIds(it)!! }
        ?.also { result.postValue(it) }
        ?.also { retrieving.postValue(false) }
}




