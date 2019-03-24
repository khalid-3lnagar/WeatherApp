package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.entties.EmptyFavoritesCities
import com.weather.entties.FavoriteCityId
import com.weather.useecasses.reposotereys.CitiesRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class UesCasesKtTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    /** use case 1 : search city by name */

    // if all is OK, trigger search
    @Test
    fun `retrieveCityByName with retrieving is false then return all Cities that matches`() {
        //Arrange
        val name = "cairo"
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(false)

        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //test result
        // result.value!!.forEach(::println)
        //Assert

        Assert.assertTrue(result.value!!.isNotEmpty())
    }


    // if is searching, then do not trigger action
    @Test
    fun `retrieveCityByName with retrieving is true then do nothing`() {
        //Arrange
        val name = "Cairo"
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(true)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //Assert
        Assert.assertTrue(result.value == null)
    }

    // city name must not be null
    @Test
    fun `retrieveCityByName with cityName is blank and retrieving is false then do nothing`() {
        //Arrange
        val name = "    "
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(false)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //Assert
        Assert.assertTrue(result.value == null)
    }

    /**use case 2 : retrieve favorite cities ids (longs) */

    // if is retrieving, then do not trigger action
    @Test
    fun `retrieveFavoriteCitiesIds with retrieving is true then do nothing`() {
        //Arrange
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        retrieving.postValue(true)
        //Act
        val result = retrieveFavoriteCitiesIds(retrieving, repositoryMock)
        //Assert
        Assert.assertTrue(result == null)


    }

    // if favorites is empty, throw an exception
    @Test(expected = EmptyFavoritesCities::class)
    fun `retrieveFavoriteCitiesIds with empty ids then throw an exception`() {
        //Arrange
        val repositoryMock = RepositoryMock()
        val retrieving = MutableLiveData<Boolean>()
        repositoryMock.favoriteCityIdsDataSource.clear()

        //Act
        retrieveFavoriteCitiesIds(retrieving, repositoryMock)

    }

    //retrieve favorite cities ids (longs)
    @Test
    fun `retrieveFavoriteCitiesIds with retrieving is false then return ids `() {
        //Arrange
        val repositoryMock = RepositoryMock()
        val retrieving = MutableLiveData<Boolean>()
        //Act
        val result = retrieveFavoriteCitiesIds(retrieving, repositoryMock)

        //Assert
        Assert.assertTrue(result != null)

    }

    /** use case 3 : retrieve cities by Ids*/

    // if is retrieving, then do not trigger action
    @Test
    fun `retrieveCitiesByIds with retrieving is true then do nothing`() {
        //Arrange

        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val ids = repositoryMock.retrieveFavoritesCitiesIds()

        val result = MutableLiveData<List<City>>()
        retrieving.postValue(true)
        //Act
        retrieveCitiesByIds(ids, retrieving, repositoryMock, result)
        //Assert

        Assert.assertTrue(result.value == null)
    }

    // if all is Ok, trigger action
    @Test
    fun `on retrieveCitiesByIds with retrieving is false then return all Cities that matches`() {
        //Arrange
        val retrieving = MutableLiveData<Boolean>()
            .also { it.postValue(false) }
        val repositoryMock = RepositoryMock()
        val ids = repositoryMock.retrieveFavoritesCitiesIds()
        val result = MutableLiveData<List<City>>()
        //Act
        retrieveCitiesByIds(ids, retrieving, repositoryMock, result)
        //Assert
        //test result
        // result.value?.forEach(::println)
        Assert.assertTrue(result.value != null)
    }

}

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

/*
fun <T> MutableList<T>.listIf(block: (field: T) -> Boolean): MutableList<T> {
    val list = mutableListOf<T>()
    forEach { if (block(it)) list.add(it) }
    return list

}*/
