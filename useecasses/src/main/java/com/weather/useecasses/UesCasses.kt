package com.weather.useecasses

import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.entties.EmptyFavoritesCities
import com.weather.entties.FavoriteCityId
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
typealias CitiesResult = MutableLiveData<List<City>>

class RetrieveCityByName(
    private val retrieving: MutableLiveData<Boolean>,
    private val repository: CitiesRepository,
    private val result: CitiesResult
) {
    operator fun invoke(cityName: String?) {
        cityName
            ?.takeUnless { retrieving.value ?: false }
            ?.takeUnless { it.isBlank() }
            ?.also { retrieving.postValue(true) }
            ?.let { repository.retrieveCitiesByName(it) }
            ?.also(result::postValue)
            ?.also { retrieving.postValue(false) }
    }

}

class RetrieveFavoriteCitiesIds(
    private val retrieving: MutableLiveData<Boolean>,
    private val repository: CitiesRepository
) {
    operator fun invoke(result: MutableLiveData<List<FavoriteCityId>>) {
        retrieving
        .takeUnless { it.value ?: false }
        ?.also { it.postValue(true) }
        ?.let { repository.retrieveFavoritesCitiesIds() }
        ?.ifEmpty { throw EmptyFavoritesCities() }
            ?.also { result.postValue(it) }
        ?.also { retrieving.postValue(false) }

    }
}

fun retrieveCitiesByIds(
    ids: List<FavoriteCityId>,
    retrieving: MutableLiveData<Boolean>,
    repository: CitiesRepository,
    result: MutableLiveData<List<City>>
) {
    ids.takeUnless { retrieving.value ?: false }
        .takeUnless { ids.isEmpty() }
        ?.also { retrieving.postValue(true) }
        ?.map { it.id }
        ?.let { repository.retrieveCitiesByIds(it) }
        ?.also { result.postValue(it) }
        ?.also { retrieving.postValue(false) }
}




