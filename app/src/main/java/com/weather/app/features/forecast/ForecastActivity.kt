package com.weather.app.features.forecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.weather.app.R
import com.weather.app.features.home.INTENT_EXTRA_CITY
import com.weather.entties.City

class ForecastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        //set title to city name
        intent
            ?.getSerializableExtra(INTENT_EXTRA_CITY)
            ?.let { (it as City).name }
            ?.also { title = it }
    }
}
