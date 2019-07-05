package com.weather.useecasses

import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.entties.EmptyFavoritesCitiesException
import com.weather.entties.FavoriteCityId
import com.weather.entties.ForecastsResponse
import com.weather.useecasses.reposotereys.*
import io.reactivex.Single

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
    private val result: CitiesResult,
    private val repository: CitiesRepository = CitiesRepositoryImplementer()
) {
    operator fun invoke(cityName: String?) {
        cityName
            ?.trim()
            ?.takeUnless { retrieving.value ?: false }
            ?.takeUnless { it.isBlank() }
            ?.also { retrieving.postValue(true) }
            ?.let { repository.retrieveCitiesByName(it) }
            ?.also(result::postValue)
            ?.also { retrieving.postValue(false) }
    }

}

class RetrieveFavoriteCities(
    private val retrieving: MutableLiveData<Boolean>,
    private val repository: CitiesRepository = CitiesRepositoryImplementer()
) {
    operator fun invoke(result: CitiesResult) {
        retrieving
            .takeUnless { it.value ?: false }
            ?.also { it.postValue(true) }
            ?.let { repository.retrieveFavoritesCitiesIds() }
            ?.ifEmpty { throw EmptyFavoritesCitiesException() }
            ?.map { it.id }
            ?.let { repository.retrieveCitiesByIds(it) }
            ?.also { result.postValue(it) }
            ?.also { retrieving.postValue(false) }

    }
}

class RetrieveCitiesByIds(
    private val retrieving: MutableLiveData<Boolean>,
    private val repository: CitiesRepository,
    private val result: CitiesResult
) {
    operator fun invoke(ids: List<FavoriteCityId>) {
        ids.takeUnless { retrieving.value ?: false }
            .takeUnless { ids.isEmpty() }
            ?.also { retrieving.postValue(true) }
            ?.map { it.id }
            ?.let { repository.retrieveCitiesByIds(it) }
            ?.also { result.postValue(it) }
            ?.also { retrieving.postValue(false) }
    }
}

class RetrieveForecastById(
    private val repository: ForecastRepository = forecastRepositoryImplementer
) {
    operator fun invoke(cityId: Long): Single<ForecastsResponse> =
        repository.retrieveCityForecastById(cityId.toString())
}

class IsFavoriteCity(
    private val repository: FavoriteRepository = FavoriteRepository()
) {

    operator fun invoke(id: Long): Boolean = repository.retrieveFavoriteCityById(id).isNotEmpty()

}

class AddFavoriteCityById(
    private val repository: FavoriteRepository = FavoriteRepository(),
    private val isFavoriteCity: IsFavoriteCity = IsFavoriteCity()
) {
    operator fun invoke(id: Long) {
        id
            .takeUnless { isFavoriteCity(id) }
            ?.also { repository.addFavoriteCityById(FavoriteCityId(it)) }

    }


}

class RemoveCityFromFavoritesById(
    private val repository: FavoriteRepository = FavoriteRepository(),
    private val isFavoriteCity: IsFavoriteCity = IsFavoriteCity()
) {
    operator fun invoke(id: Long) {
        id.takeIf { isFavoriteCity(id) }
            ?.also { repository.removeFavoritesCitiesById(FavoriteCityId(it)) }

    }
}