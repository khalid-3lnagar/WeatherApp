package com.weather.useecasses

import android.arch.persistence.room.*
import android.content.Context
import com.google.gson.Gson
import com.weather.entties.City
import com.weather.entties.Coordinates
import com.weather.entties.FavoriteCityId
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

val weatherDatabase by lazy { initializeDatabase(applicationLiveData.getApplication()) }

@Database(entities = [City::class, FavoriteCityId::class], version = 1, exportSchema = true)
@TypeConverters(CoordinatesTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val citiesDao: CitiesDao
    abstract val favoritesDao: FavoritesDao
}

class CoordinatesTypeConverter {
    @TypeConverter
    fun toJson(coordinates: Coordinates) = Gson().toJson(coordinates)

    @TypeConverter
    fun fromJson(jsonCoordinates: String) = Gson().fromJson(jsonCoordinates, Coordinates::class.java)
}

@Dao
interface FavoritesDao {

    @Query("select * from FavoriteCityId ")
    fun queryAll(): List<FavoriteCityId>

    @Insert
    fun insert(id: FavoriteCityId)

    @Delete
    fun delete(id: FavoriteCityId)
}

@Dao
interface CitiesDao {

    @Query("select * from City")
    fun queryAll(): List<City>

    @Query("select * from City where City.name like '%'||:name||'%' ")
    fun queryCitiesByName(name: String): List<City>

    @Query("select * from City where City.id in (:ids)")
    fun queryCitiesByIds(ids: List<Long>): List<City>
}

//DatabaseInitializers

private const val DATABASE_NAME = "DatabaseGateway.db"

fun initializeDatabase(context: Context): WeatherDatabase {
    copyDatabaseFileIfNotCreated(context)
    return buildDatabase(context)
}

private fun copyDatabaseFileIfNotCreated(context: Context) {
    context.getDatabasePath(DATABASE_NAME)
        ?.takeUnless { it.exists() }
        ?.let { copy(context, it) }
}


private fun copy(context: Context, databaseFile: File) {
    databaseFile.parentFile.mkdirs()
    context.assets.open(DATABASE_NAME)
        .use { copyByteArray(databaseFile, it) }
}

private fun copyByteArray(databaseFile: File, assetsInputStream: InputStream) {
    FileOutputStream(databaseFile)
        .use { it.write(byteArray(assetsInputStream)) }
}

private fun byteArray(assetsFileInputStream: InputStream) =
    ByteArray(assetsFileInputStream.available())
        .also { assetsFileInputStream.read(it) }

private fun buildDatabase(context: Context) =
    Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME).build()