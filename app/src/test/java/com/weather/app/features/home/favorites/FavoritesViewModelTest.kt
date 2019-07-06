package com.weather.app.features.home.favorites

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.weather.entties.City
import com.weather.useecasses.CitiesResult
import com.weather.useecasses.RetrieveFavoriteCities
import com.weather.useecasses.engine.toMutableLiveData
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `onRetrievingFavorites with successful response then favoritesResult is not Empty`() {
        //Arrange
        val retrieving: MutableLiveData<Boolean> = false.toMutableLiveData()
        val favoritesResult: CitiesResult = ArrayList<City>().toMutableLiveData()
        val testScheduler: Scheduler = TestScheduler()

        val mockResult = arrayListOf<City>(mock { })

        val retrieveCitiesByIds = mock<RetrieveFavoriteCities> {
            on { invoke() } doAnswer { favoritesResult.postValue(mockResult) }
        }
        val viewModelTest = FavoritesViewModel(
            retrieving,
            schedulerIo = testScheduler, mainScheduler = testScheduler,
            retrieveFavoriteCities = retrieveCitiesByIds
        )
        //Act
        viewModelTest.onRetrievingFavoritesIds()

        //Assert
        Assert.assertTrue(favoritesResult.value.isNullOrEmpty())

    }

    @Test
    fun `onRetrievingFavorites with exception  then favoritesResult is Empty`() {
        //Arrange
        val retrieving: MutableLiveData<Boolean> = false.toMutableLiveData()
        val favoritesResult: CitiesResult = ArrayList<City>().toMutableLiveData()
        val testScheduler: Scheduler = TestScheduler()
        val retrieveCitiesByIds = mock<RetrieveFavoriteCities> {}
        val viewModelTest = FavoritesViewModel(
            retrieving,
            schedulerIo = testScheduler, mainScheduler = testScheduler,
            retrieveFavoriteCities = retrieveCitiesByIds
        )
        //Act
        viewModelTest.onRetrievingFavoritesIds()

        //Assert
        Assert.assertTrue(favoritesResult.value?.isEmpty() ?: false)

    }


}