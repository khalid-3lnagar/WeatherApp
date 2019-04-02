package com.weather.useecasses

import com.nhaarman.mockitokotlin2.*
import com.weather.useecasses.reposotereys.FavoriteRepository
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong

class RemoveCityFromFavoritesByIdTest {

    @Test
    fun `removeCityFromFavoritesById with favorite city id then remove it from repository`() {
        //Arrange
        val repositoryMock = mock<FavoriteRepository> {}
        val isFavoriteCity = mock<IsFavoriteCity> {
            on { invoke(anyLong()) } doReturn true
        }
        val removeCityFromFavoritesById = RemoveCityFromFavoritesById(repositoryMock, isFavoriteCity)
        //Act
        removeCityFromFavoritesById(anyLong())

        //Assert
        verify(repositoryMock).removeFavoritesCitiesById(any())

    }

    @Test
    fun `removeCityFromFavoritesById with not Favorite City id then do nothing`() {
        //Arrange
        val repositoryMock = mock<FavoriteRepository> {}
        val isFavoriteCity = mock<IsFavoriteCity> {
            on { invoke(anyLong()) } doReturn false
        }
        val removeCityFromFavoritesById = RemoveCityFromFavoritesById(repositoryMock, isFavoriteCity)
        //Act
        removeCityFromFavoritesById(anyLong())

        //Assert
        verify(repositoryMock, never()).removeFavoritesCitiesById(any())

    }


}