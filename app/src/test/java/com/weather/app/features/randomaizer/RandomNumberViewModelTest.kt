package com.weather.app.features.randomaizer

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.weather.useecasses.DEFAULT_CLICKED_VALUE
import com.weather.useecasses.ON_CLICK_CLICKED_VALUE
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RandomNumberViewModelTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun ` on init update number liveData to DEFAULT_INCREMENTER_VALUE`() {
        //Arrange
        //Act
        val randomNumberViewModel = RandomNumberViewModel().numberLiveData
        //Assert
        Assert.assertTrue(randomNumberViewModel.value == DEFAULT_INCREMENTER_VALUE)

    }

    @Test
    fun `on init  update clickedState to DEFAULT_CLICKED_VALUE`() {
        //Arrange
        //Act
        val clickedState = RandomNumberViewModel().clickedState

        //Assert
        Assert.assertTrue(clickedState.value == DEFAULT_CLICKED_VALUE)
    }

    @Test
    fun `on incrementNumber when numberLiveData is DEFAULT_INCREMENTER_VALUE update numberLiveData value to one`() {
        //Arrange
        val viewModel = RandomNumberViewModel()
        val numberLiveData = viewModel.numberLiveData
        numberLiveData.value = DEFAULT_INCREMENTER_VALUE
        //Act
        viewModel.incrementNumber()
        //Assert
        Assert.assertTrue(numberLiveData.value == 1)

    }


    @Test
    fun `on changeClickedState when clickedState is DEFAULT_CLICKED_VALUE update clickedState to ON_CLICK_CLICKED_VALUE`() {
        //Arrange
        val viewModel = RandomNumberViewModel()
        val clickedState = viewModel.clickedState
        clickedState.postValue(DEFAULT_CLICKED_VALUE)
        //Act
        viewModel.changeClickedState()
        //Assert
        Assert.assertTrue(clickedState.value.equals(ON_CLICK_CLICKED_VALUE))
    }

    @Test
    fun `on changeClickedState when clickedState is ON_CLICK_CLICKED_VALUE update clickedState to DEFAULT_CLICKED_VALUE`() {
        //Arrange
        val viewModel = RandomNumberViewModel()
        val clickedState = viewModel.clickedState
        clickedState.postValue(ON_CLICK_CLICKED_VALUE)
        //Act
        viewModel.changeClickedState()
        //Assert
        Assert.assertTrue(clickedState.value.equals(DEFAULT_CLICKED_VALUE))
    }

}