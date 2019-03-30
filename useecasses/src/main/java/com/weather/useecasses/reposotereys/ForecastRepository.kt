package com.weather.useecasses.reposotereys

import com.weather.entties.ForecastsResponse
import com.weather.useecasses.ServerApi
import com.weather.useecasses.retrofitApi
import io.reactivex.Single

interface ForecastRepository {
    fun retrieveCityForecastById(cityId: String): Single<ForecastsResponse>
}

val forecastRepositoryImplementer by lazy { ForecastRepositoryImplementer() }

class ForecastRepositoryImplementer(private val serverApi: ServerApi = retrofitApi) : ForecastRepository {

    override fun retrieveCityForecastById(cityId: String): Single<ForecastsResponse> = serverApi.getForeCast(cityId)
}