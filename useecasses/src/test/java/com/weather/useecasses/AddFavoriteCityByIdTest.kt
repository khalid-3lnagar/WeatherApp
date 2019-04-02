package com.weather.useecasses

import com.nhaarman.mockitokotlin2.*
import com.weather.useecasses.reposotereys.FavoriteRepository
import org.junit.Test

class AddFavoriteCityByIdTest {
    @Test
    fun `addFavoriteCityById with IsFavoriteCity is false then addFavorite to database`() {
        //Arrange
        val isFavoriteCity = mock<IsFavoriteCity> {
            on { invoke(any()) } doReturn false
        }
        val repositoryMock = mock<FavoriteRepository>()
        val addFavoriteCityById = AddFavoriteCityById(repositoryMock, isFavoriteCity)
        //Act
        addFavoriteCityById(any())
        //Assert
        verify(repositoryMock).addFavoriteCityById(any())
    }

    @Test
    fun `addFavoriteCityById with IsFavoriteCity is true then do nothing`() {
        //Arrange
        val isFavoriteCity = mock<IsFavoriteCity> {
            on { invoke(any()) } doReturn true
        }
        val repositoryMock = mock<FavoriteRepository>()
        val addFavoriteCityById = AddFavoriteCityById(repositoryMock, isFavoriteCity)
        //Act
        addFavoriteCityById(any())
        //Assert
        verify(repositoryMock, never()).addFavoriteCityById(any())
    }
}