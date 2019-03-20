package com.waether.app.features.randomaizer

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RandomNumberViewModelTest {
    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `init then update number liveData to zero`() {
        //Arrange
        //Act
        val randomNumberViewModel = RandomNumberViewModel().numberLiveData

        //Assert
        Assert.assertTrue(randomNumberViewModel.value == 0)

    }

    @Test
    fun `when randomNumber is zero and incrementNumber is invoked then  numberLiveData value is one`() {
        //Arrange
        val viewModel = RandomNumberViewModel()
        val numberLiveData = viewModel.numberLiveData
        numberLiveData.value = 0
        //Act
        viewModel.incrementNumber()
        //Assert
        Assert.assertTrue(numberLiveData.value == 1)

    }
}