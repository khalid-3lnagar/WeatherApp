package com.weather.app

import android.app.Application
import com.weather.useecasses.Domain


class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Domain.integrateWith(this)

    }
}