package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.entties.FavoriteCityId
import com.weather.useecasses.reposotereys.CitiesRepositoryBase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class UesCasesKtTest {
    private val retrieving by lazy { MutableLiveData<Boolean>() }
    private val result by lazy { MutableLiveData<List<City>>() }
    private val repositoryMock by lazy { RepositoryMock() }
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // use case 1 : search city by name
    // if is searching, then do not trigger action
    // city name must not be null
    // if all is OK, trigger search
    @Test
    fun `on searchCitiesByName when retrieving is true then do nothing`() {
        //Arrange
        val name = "Cairo"
        retrieving.postValue(true)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)

        //Assert

        Assert.assertTrue(result.value!!.isEmpty())
    }

    @Test
    fun `on searchCitiesByName when cityName is blank and not retrieving then do nothing`() {
        //Arrange
        val name = "    "
        retrieving.postValue(false)
        result.postValue(emptyList())

        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)

        //Assert
        Assert.assertTrue(result.value!!.isEmpty())
    }

    @Test
    fun `on searchCitiesByName when is not retrieving and cityName in repository then return all Cities that cityName in it`() {
        //Arrange
        val name = "cairo"
        retrieving.postValue(false)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //test result
        // result.value!!.forEach(::println)
        //Assert
        Assert.assertTrue(result.value!!.isNotEmpty())
    }

    // usecase 2 : retrieve favorite cities ids (longs)
    // if is retrieving, then do not trigger action
    // if favorites is empty, throw an exception
    // if favorites not empty, convert them to ids (longs)

    // usecase 3 : retrieve cities by Ids
    // if is retrieving, then do not trigger action
    // if all is Ok, trigger action


}

class RepositoryMock : CitiesRepositoryBase {
    private val citiesDataSource by lazy {
        mutableListOf<City>()
            .apply {
                add(City(1, "cairo", "egypt", coordinates = null))
                add(City(2, "Shobra cairo ", "egypt", coordinates = null))
                add(City(3, "Alexandria", "egypt", coordinates = null))
            }
    }
    private val favoriteCityIdsDataSource by lazy {
        mutableListOf<FavoriteCityId>()
            .also { it.add(FavoriteCityId(1)) }
            .also { it.add(FavoriteCityId(2)) }
    }

    private fun <T> MutableList<T>.listIf(block: (field: T) -> Boolean): MutableList<T> {
        val list = mutableListOf<T>()
        forEach { if (block(it)) list.add(it) }
        return list

    }

    override fun retrieveCitiesByName(name: String): List<City> = citiesDataSource.listIf { it.name!!.contains(name) }

    override fun retrieveCitiesIds(cityIds: List<Long>): List<City> = citiesDataSource.listIf { it.id in cityIds }

    override fun retrieveFavoritesCitiesIds(): List<FavoriteCityId> = favoriteCityIdsDataSource


}
