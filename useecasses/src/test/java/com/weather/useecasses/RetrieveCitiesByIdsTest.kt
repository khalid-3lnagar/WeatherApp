package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RetrieveCitiesByIdsTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()


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

        val retrieveCitiesByIds = RetrieveCitiesByIds(retrieving, result, repositoryMock)
        //Act
        retrieveCitiesByIds(ids)
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

        val retrieveCitiesByIds = RetrieveCitiesByIds(retrieving, result, repositoryMock)
        //Act
        retrieveCitiesByIds(ids)
        //Assert
        //test result
        // result.value?.forEach(::println)
        Assert.assertTrue(result.value != null)
    }

}