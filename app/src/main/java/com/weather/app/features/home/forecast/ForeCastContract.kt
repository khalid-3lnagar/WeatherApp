package com.weather.app.features.home.forecast

import com.weather.entties.City
import com.weather.entties.Forecast

//View  > Presenter > Model
interface ForecastPresenter {
    fun initializeCity(city: City)
    fun addCityToFavorites()
    fun removeCityFromFavorites()


}

interface ForecastView {
    fun setCityTitle(cityName: String)
    fun startLoading()
    fun stopLoading()
    fun drawForecastList(forecastList: List<Forecast>)
    fun drawErrorImage()
    fun drawAsFavoriteCity()
    fun drawAsNotFavoriteCity()


}
