package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.weather.entties.EmptyFavoritesCities
import com.weather.entties.FavoriteCityId
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
    fun `retrieveFavoriteCitiesIds with retrieving is true then do nothing`() {
        //Arrange
        val retrieving = true.toMutableLiveData()
        val result = listOf<FavoriteCityId>().toMutableLiveData()
        val repositoryMock = RepositoryMock()

        val retrieveFavoriteCitiesIds = RetrieveFavoriteCitiesIds(retrieving, repositoryMock)

        //Act
        retrieveFavoriteCitiesIds(result)
        //Assert
        Assert.assertTrue(result.value!!.isEmpty())


    }

    // if favorites is empty, throw an exception
    @Test(expected = EmptyFavoritesCities::class)
    fun `retrieveFavoriteCitiesIds with empty ids then throw an exception`() {
        //Arrange
        val retrieving = false.toMutableLiveData()
        val result = listOf<FavoriteCityId>().toMutableLiveData()
        val repositoryMock = RepositoryMock()
        repositoryMock.favoriteCityIdsDataSource.clear()

        val retrieveFavoriteCitiesIds = RetrieveFavoriteCitiesIds(retrieving, repositoryMock)
        //Act
        retrieveFavoriteCitiesIds(result)

    }

    //retrieve favorite cities ids (longs)
    @Test
    fun `retrieveFavoriteCitiesIds with retrieving is false then return ids `() {
        val retrieving = false.toMutableLiveData()
        val result = listOf<FavoriteCityId>().toMutableLiveData()
        val repositoryMock = RepositoryMock()

        val retrieveFavoriteCitiesIds = RetrieveFavoriteCitiesIds(retrieving, repositoryMock)

        //Act
        retrieveFavoriteCitiesIds(result)
        //Assert
        Assert.assertTrue(result.value!!.isNotEmpty())

    }


}