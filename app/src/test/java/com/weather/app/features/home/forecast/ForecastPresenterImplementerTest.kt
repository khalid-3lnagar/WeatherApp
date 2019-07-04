package com.weather.app.features.home.forecast

import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.weather.entties.City
import com.weather.entties.ForecastsResponse
import com.weather.useecasses.AddFavoriteCityById
import com.weather.useecasses.IsFavoriteCity
import com.weather.useecasses.RemoveCityFromFavoritesById
import com.weather.useecasses.RetrieveForecastById
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito.verify

class ForecastPresenterImplementerTest {


    @Test
    fun `initializeCity then update viewTitle`() {

        //Arrange
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
        }
        val responseMock = Single.just<ForecastsResponse>(
            ForecastsResponse(cityMock, 0, listOf())
        )

        val viewMock = mock<ForecastView>()

        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(cityMock.id)) } doReturn responseMock
        }

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
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
        }
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))

        val viewMock = mock<ForecastView>()

        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(cityMock.id)) } doReturn responseMock
        }
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock, atLeast(1)).startLoading()

    }

    @Test
    fun `initializeCity with success response then invoke drawForecastList`() {

        //Arrange
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
        }
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))

        val viewMock = mock<ForecastView>()

        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(cityMock.id)) } doReturn responseMock
        }
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
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
        }
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, null))

        val viewMock = mock<ForecastView>()

        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(cityMock.id)) } doReturn responseMock
        }
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock, atLeast(1)).drawErrorImage()
    }

    @Test
    fun `initializeCity when response is handled stop loading`() {

        //Arrange
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
        }
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))

        val viewMock = mock<ForecastView>()

        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(cityMock.id)) } doReturn responseMock
        }
        val presenter = ForecastPresenterImplementer(viewMock, retrieveForecastById, testScheduler, testScheduler)

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock, atLeast(1)).stopLoading()

    }

    @Test
    fun `initializeCity with city is Favorite City then drawAsFavoriteCity`() {

        //Arrange
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
            on { id } doReturn 123
        }
        val viewMock = mock<ForecastView>()
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))

        val isFavoriteCity = mock<IsFavoriteCity> {
            on { invoke(eq(123)) } doReturn true
        }
        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(123)) } doReturn responseMock
        }
        val presenter =
            ForecastPresenterImplementer(
                view = viewMock,
                retrieveForecastById = retrieveForecastById,
                schedulerIo = testScheduler,
                mainScheduler = testScheduler,
                isFavoriteCity = isFavoriteCity
            )

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).drawAsFavoriteCity()

    }

    @Test
    fun `initializeCity with city is not Favorite city then drawAsNotFavoriteCity`() {
        //Arrange
        val testScheduler = TestScheduler()

        val cityMock = mock<City> {
            on { name } doReturn "cairo"
            on { id } doReturn 123
        }
        val viewMock = mock<ForecastView>()
        val responseMock = Single.just<ForecastsResponse>(ForecastsResponse(cityMock, 0, listOf()))

        val isFavoriteCity = mock<IsFavoriteCity> {
            on { invoke(eq(123)) } doReturn false
        }
        val retrieveForecastById = mock<RetrieveForecastById> {
            on { invoke(eq(123)) } doReturn responseMock
        }
        val presenter =
            ForecastPresenterImplementer(
                view = viewMock,
                retrieveForecastById = retrieveForecastById,
                schedulerIo = testScheduler,
                mainScheduler = testScheduler,
                isFavoriteCity = isFavoriteCity
            )

        //Act
        presenter.initializeCity(cityMock)
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).drawAsNotFavoriteCity()

    }

    @Test
    fun `addCityToFavorites then drawAsFavoriteCity`() {
        //Arrange
        val testScheduler = TestScheduler()
        val viewMock = mock<ForecastView>()
        val cityMock = mock<City> { on { id } doReturn 123 }
        val addFavoriteCityByIdMock = mock<AddFavoriteCityById>()
        val presenter = ForecastPresenterImplementer(
            view = viewMock,
            forecastCity = cityMock,
            schedulerIo = testScheduler,
            mainScheduler = testScheduler,
            addFavoriteCityById = addFavoriteCityByIdMock
        )

        //Act
        presenter.addCityToFavorites()
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).drawAsFavoriteCity()
    }

    @Test
    fun `removeCityFromFavorites then drawAsNotFavoriteCity`() {
        //Arrange
        val testScheduler = TestScheduler()
        val viewMock = mock<ForecastView> {}
        val cityMock = mock<City> { on { id } doReturn 123L }
        val removeCityFromFavorites = mock<RemoveCityFromFavoritesById> {}
        val presenter = ForecastPresenterImplementer(
            view = viewMock,
            schedulerIo = testScheduler,
            mainScheduler = testScheduler,
            forecastCity = cityMock,
            removeCityFromFavoritesById = removeCityFromFavorites
        )
        //Act
        presenter.removeCityFromFavorites()
        testScheduler.triggerActions()

        //Assert
        verify(viewMock).drawAsNotFavoriteCity()
    }

}