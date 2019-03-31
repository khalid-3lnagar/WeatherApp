package com.weather.useecasses

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.weather.entties.ForecastsResponse
import com.weather.useecasses.reposotereys.ForecastRepositoryImplementer
import io.reactivex.Single
import org.junit.Test

class RetrieveForecastByIdTest {

    private val mockResponse = Single.just(ForecastsResponse(null, null, null))
    @Test
    fun `retrieveForecastById with valid city id then success response`() {

        //Arrange
        val mockApi = mock<ServerApi> { on { getForeCast(any(), any()) } doReturn mockResponse }
        val repository = ForecastRepositoryImplementer(mockApi)
        val retrieveForecastById = RetrieveForecastById(repository)

        //Act
        retrieveForecastById(123L)

        //Assert
        verify(mockApi).getForeCast(any(), any())

    }

}