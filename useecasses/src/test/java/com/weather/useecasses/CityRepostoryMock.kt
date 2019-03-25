package com.weather.useecasses

import com.weather.entties.City
import com.weather.entties.FavoriteCityId
import com.weather.useecasses.reposotereys.CitiesRepository

class RepositoryMock : CitiesRepository {
    private val citiesDataSource by lazy {
        mutableListOf<City>(
            City(1, "cairo", "egypt", coordinates = null),
            City(2, "Shobra cairo ", "egypt", coordinates = null),
            City(3, "Alexandria", "egypt", coordinates = null)
        )
    }
    val favoriteCityIdsDataSource by lazy {
        mutableListOf<FavoriteCityId>(FavoriteCityId(1), FavoriteCityId(2))
    }


    override fun retrieveCitiesByName(name: String): List<City> = citiesDataSource.filter { it.name!!.contains(name) }

    override fun retrieveCitiesByIds(cityIds: List<Long>): List<City> = citiesDataSource.filter { it.id in cityIds }

    override fun retrieveFavoritesCitiesIds(): List<FavoriteCityId> = favoriteCityIdsDataSource


}

