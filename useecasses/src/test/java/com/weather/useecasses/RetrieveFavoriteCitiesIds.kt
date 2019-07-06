package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.weather.entties.City
import com.weather.entties.EmptyFavoritesCitiesException
import com.weather.useecasses.engine.toMutableLiveData
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RetrieveFavoriteCitiesIdsTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    /**use case 2 : retrieve favorite cities ids (longs) */

    // if is retrieving, then do not trigger action
    @Test
    fun `retrieveFavoriteCities with retrieving is true then do nothing`() {
        //Arrange
        val retrieving = true.toMutableLiveData()
        val result = listOf<City>().toMutableLiveData()
        val repositoryMock = RepositoryMock()

        val retrieveFavoriteCities = RetrieveFavoriteCities(retrieving, result, repositoryMock)

        //Act
        retrieveFavoriteCities()
        //Assert
        Assert.assertTrue(result.value!!.isEmpty())


    }

    // if favorites is empty, throw an exception
    @Test(expected = EmptyFavoritesCitiesException::class)
    fun `retrieveFavoriteCities with empty ids then throw an exception`() {
        //Arrange
        val retrieving = false.toMutableLiveData()
        val result = listOf<City>().toMutableLiveData()
        val repositoryMock = RepositoryMock()
        repositoryMock.favoriteCityIdsDataSource.clear()

        val retrieveFavoriteCities = RetrieveFavoriteCities(retrieving, result, repositoryMock)
        //Act
        retrieveFavoriteCities()

    }

    //retrieve favorite cities ids (longs)
    @Test
    fun `retrieveFavoriteCities with retrieving is false then return ids `() {
        val retrieving = false.toMutableLiveData()
        val result = listOf<City>().toMutableLiveData()
        val repositoryMock = RepositoryMock()

        val retrieveFavoriteCitiesIds = RetrieveFavoriteCities(retrieving, result, repositoryMock)
        //Act
        retrieveFavoriteCitiesIds()
        //Assert
        Assert.assertTrue(result.value!!.isNotEmpty())

    }


}