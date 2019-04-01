package com.weather.app.features.forecast

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.weather.app.R
import com.weather.app.features.home.INTENT_EXTRA_CITY
import com.weather.entties.City
import com.weather.entties.Forecast
import com.weather.useecasses.RetrieveForecastById
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forecast.*

class ForecastActivity : AppCompatActivity(), ForecastView {

    private val presenter by lazy { ForecastPresenterImplementer(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        lifecycle.addObserver(presenter)
        presenter.initializeCity(getCity()!!)
        errorImage.setOnClickListener { finish() }
        favoriteFloatingBtn.setOnClickListener { onFavoriteBtnClicked(it) }
    }

    private fun onFavoriteBtnClicked(it: View) {
        if (it.isSelected) {
            drawAsNotFavoriteCity()
            it.isSelected = false
        } else {
            drawAsFavoriteCity()
            it.isSelected = true
        }
    }

    private fun getCity() = intent?.getSerializableExtra(INTENT_EXTRA_CITY)?.let { (it as City) }

    override fun setCityTitle(cityName: String) {
        title = cityName
    }

    override fun startLoading() {
        forecastTxt.visibility = GONE
        favoriteFloatingBtn.hide()
        forecastLoading.visibility = VISIBLE
    }

    override fun drawForecastList(forecastList: List<Forecast>) {

        val builder = StringBuilder()
        forecastList.forEach { builder.append("\n\n\n$it") }
        forecastTxt.text = builder.toString()
        favoriteFloatingBtn.show()

    }

    override fun drawErrorImage() {
        favoriteFloatingBtn.hide()
        errorImage.visibility = View.VISIBLE
    }


    override fun stopLoading() {
        forecastTxt.visibility = VISIBLE
        forecastLoading.visibility = GONE
    }

    override fun drawAsFavoriteCity() {
        favoriteFloatingBtn.setImageResource(R.drawable.ic_favorite)
    }

    override fun drawAsNotFavoriteCity() {
        favoriteFloatingBtn.setImageResource(R.drawable.ic_not_favorite)
    }

}


class ForecastPresenterImplementer(
    private val view: ForecastView,
    private val retrieveForecastById: RetrieveForecastById = RetrieveForecastById(),
    private val schedulerIo: Scheduler = Schedulers.io(),
    private val mainScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ForecastPresenter, DefaultLifecycleObserver {
    private val disposables by lazy { CompositeDisposable() }
    private var forecastCity: City? = null

    override fun initializeCity(city: City) {
        city
            .also { forecastCity = it }
            .also { view.setCityTitle(it.name!!) }
            .also { retrieveForecastInBackground(it.id) }


    }

    private fun retrieveForecastInBackground(id: Long) {
        id
            .also { view.startLoading() }
            .let { retrieveForecastById(it) }
            .subscribeOn(schedulerIo)
            .observeOn(mainScheduler)
            .map { it.forecasts }
            .doFinally { view.stopLoading() }
            .subscribe({ view.drawForecastList(it!!) }, { view.drawErrorImage() })
            .also { disposables.add(it) }
    }

    override fun addCityToFavorites() {

    }

    override fun removeCityFromFavorites() {

    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }
}


