package com.weather.useecasses

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.weather.entties.City
import com.weather.entties.FavoriteCityId
import com.weather.useecasses.reposotereys.CitiesRepositoryBase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class UesCasesKtTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    /** use case 1 : search city by name */

    // if is searching, then do not trigger action
    @Test
    fun `on retrieveCityByName when retrieving is true then do nothing`() {
        //Arrange
        val name = "Cairo"
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(true)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //Assert
        Assert.assertTrue(result.value == null)
    }

    // city name must not be null
    @Test
    fun `on retrieveCityByName when cityName is blank and not retrieving then do nothing`() {
        //Arrange
        val name = "    "
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(false)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //Assert
        Assert.assertTrue(result.value == null)
    }

    // if all is OK, trigger search
    @Test
    fun `on retrieveCityByName when is not retrieving and cityName in repository then return all Cities that cityName in it`() {
        //Arrange
        val name = "cairo"
        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val result = MutableLiveData<List<City>>()
        retrieving.postValue(false)
        //Act
        retrieveCityByName(name, retrieving, repositoryMock, result)
        //test result
        // result.value!!.forEach(::println)
        //Assert

        Assert.assertTrue(result.value != null)
    }

    /**
     *  usecase 2 : retrieve favorite cities ids (longs)
     *  */
    // if is retrieving, then do not trigger action
    @Test
    fun `on retrieveFavoriteCitiesIds when retrieving is true do nothing`() {
        //Arrange

        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        retrieving.postValue(true)
        //Act
        val result = retrieveFavoriteCitiesIds(retrieving, repositoryMock)
        Assert.assertTrue(result == null)


    }

    // if favorites is empty, throw an exception
    @Test
    fun `on retrieveFavoriteCitiesIds when FavoriteCitiesIds is empty, throw an exception`() {
        //Arrange
        val repositoryMock = RepositoryMock()
        val retrieving = MutableLiveData<Boolean>()
        repositoryMock.favoriteCityIdsDataSource.clear()
        try {
            //Act
            retrieveFavoriteCitiesIds(retrieving, repositoryMock)
            Assert.fail("Should have thrown Exception")
        } catch (e: Exception) {
            //Assert
            Assert.assertTrue(true)

        }

    }

    //retrieve favorite cities ids (longs)
    @Test
    fun `on retrieveFavoriteCitiesIds when  retrieving is false and FavoriteCitiesIds is not Empty then get FavoriteCitiesIds `() {
        //Arrange
        val repositoryMock = RepositoryMock()
        val retrieving = MutableLiveData<Boolean>()
        //Act
        val result = retrieveFavoriteCitiesIds(retrieving, repositoryMock)

        //Assert
        Assert.assertTrue(result != null)

    }

    /**
     * usecase 3 : retrieve cities by Ids
     * */

    // if is retrieving, then do not trigger action
    @Test
    fun `on retrieveCitiesByIds when retrieving is true then do nothing`() {
        //Arrange

        val retrieving = MutableLiveData<Boolean>()
        val repositoryMock = RepositoryMock()
        val ids = repositoryMock.retrieveFavoritesCitiesIds()

        val result = MutableLiveData<List<City>>()
        retrieving.postValue(true)
        //Act
        retrieveCitiesByIds(ids, retrieving, repositoryMock, result)
        //Assert

        Assert.assertTrue(result.value == null)
    }

    // if all is Ok, trigger action
    @Test
    fun `on retrieveCitiesByIds when retrieving is false then get citiesIds`() {
        //Arrange
        val retrieving = MutableLiveData<Boolean>()
            .also { it.postValue(false) }
        val repositoryMock = RepositoryMock()
        val ids = repositoryMock.retrieveFavoritesCitiesIds()
        val result = MutableLiveData<List<City>>()
        //Act
        retrieveCitiesByIds(ids, retrieving, repositoryMock, result)
        //Assert
        //test result
        result.value?.forEach(::println)
        Assert.assertTrue(result.value != null)
    }

}

class RepositoryMock : CitiesRepositoryBase {
    private val citiesDataSource by lazy {
        mutableListOf<City>()
            .apply {
                add(City(1, "cairo", "egypt", coordinates = null))
                add(City(2, "Shobra cairo ", "egypt", coordinates = null))
                add(City(3, "Alexandria", "egypt", coordinates = null))
            }
    }
    val favoriteCityIdsDataSource by lazy {
        mutableListOf<FavoriteCityId>()
            .apply {
                add(FavoriteCityId(1))
                add(FavoriteCityId(2))
            }
    }

    private fun <T> MutableList<T>.listIf(block: (field: T) -> Boolean): MutableList<T> {
        val list = mutableListOf<T>()
        forEach { if (block(it)) list.add(it) }
        return list

    }

    override fun retrieveCitiesByName(name: String): List<City> = citiesDataSource.listIf { it.name!!.contains(name) }

    override fun retrieveCitiesByIds(cityIds: List<Long>): List<City> = citiesDataSource.listIf { it.id in cityIds }

    override fun retrieveFavoritesCitiesIds(): List<FavoriteCityId> = favoriteCityIdsDataSource


}
