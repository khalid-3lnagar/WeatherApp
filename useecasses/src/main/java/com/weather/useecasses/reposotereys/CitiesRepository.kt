package com.weather.useecasses.reposotereys

import com.weather.entties.FavoriteCityId
import com.weather.useecasses.WeatherDatabase
import com.weather.useecasses.weatherDatabase

val citiesRepository by lazy { CitiesRepository() }

class CitiesRepository(private val database: Lazy<WeatherDatabase> = lazy { weatherDatabase }) {

    fun searchCitiesByName(name: String) = database.value.citiesDao.queryCitiesByName(name)


    fun retrieveCitiesIds(cityIds: List<Long>) = database.value.citiesDao.queryCitiesByIds(cityIds)

    fun retrieveFavoritesCitiesIds() = database.value.favoritesDao.queryAll()

    fun addFavoriteCityById(favoriteCityId: FavoriteCityId) = database.value.favoritesDao.insert(favoriteCityId)

    fun removeFavoritesCitiesById(favoriteCityId: FavoriteCityId) = database.value.favoritesDao.delete(favoriteCityId)

}
