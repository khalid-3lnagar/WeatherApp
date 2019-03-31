package com.weather.useecasses

import com.weather.entties.ForecastsResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//OKhTTP INTERCEPTOR
//O_AUTH 2

// Multi thread
//operates
//reActive programming
//performance


private const val APP_ID_KEY = "appid"
private const val APP_ID_VALUE = "cc8bf0ef9fefd3794a362f69e9b0721d"
private const val OPEN_WEATHER_MAPS_URL = "https://api.openweathermap.org"

interface ServerApi {
    @GET("/data/2.5/forecast")
    fun getForeCast(
        @Query("id") cityId: String
        , @Query(APP_ID_KEY) appIdValue: String = APP_ID_VALUE
    ): Single<ForecastsResponse>
}

private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(OPEN_WEATHER_MAPS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}
val retrofitApi: ServerApi by lazy { retrofit.create(ServerApi::class.java) }

