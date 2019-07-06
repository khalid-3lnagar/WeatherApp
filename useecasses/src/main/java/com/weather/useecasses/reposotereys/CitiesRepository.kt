package com.weather.useecasses.reposotereys

import com.weather.entties.City
import com.weather.entties.FavoriteCityId
import com.weather.useecasses.WeatherDatabase
import com.weather.useecasses.weatherDatabase

class CitiesRepositoryImplementer(private val database: Lazy<WeatherDatabase> = lazy { weatherDatabase }) :
    CitiesRepository {
    override fun retrieveCitiesByName(name: String): List<City> = database.value.citiesDao.queryCitiesByName(name)

    override fun retrieveCitiesByIds(cityIds: List<Long>) = database.value.citiesDao.queryCitiesByIds(cityIds)

    override fun retrieveFavoritesCitiesIds() = database.value.favoritesDao.queryAll()

}

class FavoriteRepository(private val database: Lazy<WeatherDatabase> = lazy { weatherDatabase }) {

    fun addFavoriteCityById(favoriteCityId: FavoriteCityId) = database.value.favoritesDao.insert(favoriteCityId)

    fun removeFavoritesCitiesById(favoriteCityId: FavoriteCityId) = database.value.favoritesDao.delete(favoriteCityId)

    fun retrieveFavoriteCityById(id: Long) = database.value.favoritesDao.queryFavoriteById(id)
}

interface CitiesRepository {

    fun retrieveCitiesByName(name: String): List<City>

    fun retrieveFavoritesCitiesIds(): List<FavoriteCityId>

    fun retrieveCitiesByIds(cityIds: List<Long>): List<City>

}
