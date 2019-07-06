package com.weather.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import com.weather.app.features.home.BROADCAST_ACTION_SHOW_CITY
import com.weather.app.features.home.INTENT_EXTRA_CITY
import com.weather.app.features.home.forecast.ForecastActivity
import com.weather.entties.City
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

fun sendShowCityBroadcast(city: City, view: View) {
    Intent(BROADCAST_ACTION_SHOW_CITY)
        .putExtra(INTENT_EXTRA_CITY, city as Serializable)
        .also { view.context.sendBroadcast(it) }
}

fun startForeCastActivity(packageContext: Context, city: Serializable?) {
    Intent(packageContext, ForecastActivity::class.java)
        .apply { putExtra(INTENT_EXTRA_CITY, city) }
        .also { packageContext.startActivity(it) }
}

class ShowCityReceiver(private val showCity: PublishSubject<Serializable>) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        showCity.onNext(intent?.getSerializableExtra(INTENT_EXTRA_CITY)!!)
    }
}
