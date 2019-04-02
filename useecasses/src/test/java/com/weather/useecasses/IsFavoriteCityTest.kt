package com.weather.useecasses

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.weather.entties.FavoriteCityId
import com.weather.useecasses.reposotereys.FavoriteRepository
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong

class IsFavoriteCityTest {
    @Test
    fun `isFavoriteCity with not empty list then return true`() {
        //Arrange
        val repositoryMock = mock<FavoriteRepository> {
            on { retrieveFavoriteCityById(anyLong()) } doReturn listOf(FavoriteCityId(1))
        }
        val isFavoriteCity = IsFavoriteCity(repositoryMock)

        //Act
        val result = isFavoriteCity(anyLong())

        //Assert
        Assert.assertTrue(result)
    }

    @Test
    fun `isFavoriteCity with empty list then return false`() {
        //Arrange
        val repositoryMock = mock<FavoriteRepository> {
            on { retrieveFavoriteCityById(anyLong()) } doReturn listOf()
        }
        val isFavoriteCity = IsFavoriteCity(repositoryMock)

        //Act
        val result = isFavoriteCity(anyLong())

        //Assert
        Assert.assertFalse(result)
    }
}