package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RetrieveCityByNameTest {
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

        val retrieveCityByNameTest = RetrieveCityByName(retrieving, result, repositoryMock)

        //Act
        retrieveCityByNameTest(name)
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

        val retrieveCityByNameTest = RetrieveCityByName(retrieving, result, repositoryMock)

        //Act
        retrieveCityByNameTest(name)

        //Assert
        Assert.assertTrue(result.value == null)
    }

    // city name must not be null
    @Test
    fun `retrieveCityByName with cityName is blank then do nothing`() {
        //Arrange
        val name = "    "
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(false)

        val retrieveCityByNameTest = RetrieveCityByName(retrieving, result, repositoryMock)

        //Act
        retrieveCityByNameTest(name)
        //Assert
        Assert.assertTrue(result.value == null)
    }

}
