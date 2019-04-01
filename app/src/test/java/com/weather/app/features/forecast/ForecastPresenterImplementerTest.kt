package com.weather.app.features.forecast

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.weather.entties.City
import com.weather.entties.ForecastsResponse
import com.weather.useecasses.RetrieveForecastById
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito.verify

class ForecastPresenterImplementerTest {

    private val cityMock by lazy { mock<City>() }

    private val viewMock by lazy { mock<ForecastView>() }

    private val retrieveForecastById by lazy { mock<RetrieveForecastById>() }

    private val testScheduler by lazy { TestScheduler() }

    @Test
    fun `initializeCity then update viewTitle`() {

        //Arrange
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))
        given { cityMock.name }.willReturn("cairo")
        given { retrieveForecastById.invoke(eq(cityMock.id)) }.willReturn(responseMock)
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).setCityTitle("cairo")

    }

    @Test
    fun `initializeCity then start loading`() {

        //Arrange
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))
        given { cityMock.name }.willReturn("cairo")
        given { retrieveForecastById.invoke(eq(cityMock.id)) }.willReturn(responseMock)
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).startLoading()

    }

    @Test
    fun `initializeCity with success response then invoke drawForecastList`() {

        //Arrange
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))
        given { cityMock.name }.willReturn("cairo")
        given { retrieveForecastById.invoke(eq(cityMock.id)) }.willReturn(responseMock)
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).drawForecastList(listOf())

    }

    @Test
    fun `initializeCity with forecast equals null then invoke drawErrorImage`() {

        //Arrange
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, null))
        given { cityMock.name }.willReturn("cairo")
        given { retrieveForecastById.invoke(eq(cityMock.id)) }.willReturn(responseMock)
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).drawErrorImage()
    }


    @Test
    fun `initializeCity when response is handled stop loading`() {

        //Arrange
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))
        given { cityMock.name }.willReturn("cairo")
        given { retrieveForecastById.invoke(eq(cityMock.id)) }.willReturn(responseMock)
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).stopLoading()

    }

}