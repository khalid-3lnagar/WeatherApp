package com.weather.useecasses

import android.arch.persistence.room.*
import com.google.gson.Gson
import com.weather.entties.City
import com.weather.entties.Coordinates
import com.weather.entties.FavoriteCityId

@Database(entities = [City::class, FavoriteCityId::class], version = 1, exportSchema = true)
@TypeConverters(CoordinatesTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val citiesDao: CitiesDao
    abstract val favoritesDao: FavoritesDao
}

@Dao
interface FavoritesDao {
    @Query("select * from FavoriteCityId ")
    fun queryAll(): List<Long>
}

@Dao
interface CitiesDao {
    @Query("select * from City where City.name like :name ")
    fun queryCitiesByName(name: String): List<City>

    @Query("select * from City where City.id in (:ids)")
    fun queryCitiesByIds(ids: List<Int>): List<City>

}


class CoordinatesTypeConverter {
    @TypeConverter
    fun toJson(coordinates: Coordinates) = Gson().toJson(coordinates)

    @TypeConverter
    fun fromJson(jsonCoordinates: String) = Gson().fromJson(jsonCoordinates, Coordinates::class.java)
}
