package com.weather.app.features.home.forecast

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.weather.app.R
import com.weather.app.features.home.INTENT_EXTRA_CITY
import com.weather.entties.City
import com.weather.entties.Forecast
import com.weather.useecasses.AddFavoriteCityById
import com.weather.useecasses.IsFavoriteCity
import com.weather.useecasses.RemoveCityFromFavoritesById
import com.weather.useecasses.RetrieveForecastById
import io.reactivex.Completable.fromCallable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forecast.*
import kotlinx.android.synthetic.main.item_forecast.view.*

//region View
class ForecastActivity : AppCompatActivity(), ForecastView {

    private val presenter by lazy { ForecastPresenterImplementer(this) }
    private val forecastAdapter by lazy { ForecastAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        lifecycle.addObserver(presenter)
        presenter.initializeCity(getCity()!!)
        errorImage.setOnClickListener { finish() }
        fab_favorite_forecast.setOnClickListener { onFavoriteBtnClicked(it) }

        initRecyclerView()

    }

    private fun initRecyclerView() {
        with(rv_forecast) {
            layoutManager = LinearLayoutManager(this@ForecastActivity)
            adapter = forecastAdapter
        }
    }

    private fun onFavoriteBtnClicked(it: View) {
        if (it.isSelected)
            presenter.removeCityFromFavorites()
        else
            presenter.addCityToFavorites()

    }

    private fun getCity() = intent?.getSerializableExtra(INTENT_EXTRA_CITY)?.let { (it as City) }

    override fun setCityTitle(cityName: String) {
        title = cityName
    }

    override fun startLoading() {
        rv_forecast.visibility = INVISIBLE
        fab_favorite_forecast.hide()
        forecastLoading.visibility = VISIBLE
    }

    override fun drawForecastList(forecastList: List<Forecast>?) {

        with(rv_forecast) {
            forecastList.takeUnless { it.isNullOrEmpty() }
                ?.let { forecastAdapter }
                ?.also { it.forecasts.clear() }
                ?.also { it.forecasts.addAll(forecastList!!) }
                ?.apply { notifyDataSetChanged() }
                ?.also { adapter = it } ?: drawErrorImage()
            visibility = VISIBLE
        }
        fab_favorite_forecast.show()

    }

    override fun drawErrorImage() {
        fab_favorite_forecast.hide()
        errorImage.visibility = VISIBLE
    }

    override fun stopLoading() {
        //   forecastTxt.visibility = VISIBLE
        forecastLoading.visibility = GONE
    }

    override fun drawAsFavoriteCity() {
        with(fab_favorite_forecast) {
            setImageResource((R.drawable.ic_favorite))
            isSelected = true

        }


    }

    override fun drawAsNotFavoriteCity() {
        with(fab_favorite_forecast) {
            setImageResource(R.drawable.ic_not_favorite)
            isSelected = false
        }
    }

}

//endregion

//region Presenter
class ForecastPresenterImplementer(
    private val view: ForecastView,
    private val retrieveForecastById: RetrieveForecastById = RetrieveForecastById(),
    private val schedulerIo: Scheduler = Schedulers.io(),
    private val mainScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val isFavoriteCity: IsFavoriteCity = IsFavoriteCity(),
    private val addFavoriteCityById: AddFavoriteCityById = AddFavoriteCityById(),
    private val removeCityFromFavoritesById: RemoveCityFromFavoritesById = RemoveCityFromFavoritesById(),
    private var forecastCity: City? = null
) : ForecastPresenter, DefaultLifecycleObserver {
    private val disposables by lazy { CompositeDisposable() }

    override fun initializeCity(city: City) {
        city
            .also { forecastCity = it }
            .also { view.setCityTitle(it.name!!) }
            .let { it.id }
            .also { checkIfFavoriteCity(it) }
            .also { retrieveForecastInBackground(it) }

    }

    private fun checkIfFavoriteCity(id: Long) {

        id
            .let { Single.fromCallable { isFavoriteCity(it) } }
            .subscribeOn(schedulerIo)
            .observeOn(mainScheduler)
            .subscribe({ if (it) view.drawAsFavoriteCity() else view.drawAsNotFavoriteCity() },
                { view.drawErrorImage() })
            .also { disposables.add(it) }
    }

    private fun retrieveForecastInBackground(id: Long) {
        id
            .also { view.startLoading() }
            .let { retrieveForecastById(it) }
            .subscribeOn(schedulerIo)
            .observeOn(mainScheduler)
            .map { it.forecasts }
            .doFinally { view.stopLoading() }
            .subscribe({ view.drawForecastList(it) }, { view.drawErrorImage() })
            .also { disposables.add(it) }
    }

    override fun addCityToFavorites() {
        fromCallable { addFavoriteCityById(forecastCity!!.id) }
            .subscribeOn(schedulerIo)
            .observeOn(mainScheduler)
            .subscribe({ view.drawAsFavoriteCity() }, { view.drawErrorImage() })
            .also { disposables.add(it) }

    }

    override fun removeCityFromFavorites() {
        fromCallable { removeCityFromFavoritesById(forecastCity!!.id) }
            .subscribeOn(schedulerIo)
            .observeOn(mainScheduler)
            .subscribe({ view.drawAsNotFavoriteCity() }, { view.drawErrorImage() })
            .also { disposables.add(it) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }
}
//endregion

//region ForecastAdapter
class ForecastAdapter(
    val forecasts: MutableList<Forecast> = mutableListOf()
) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, posstion: Int): ForecastViewHolder =
        LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_forecast, viewGroup, false)
            .let { ForecastViewHolder(it) }


    override fun getItemCount(): Int = forecasts.size

    override fun onBindViewHolder(viewHolder: ForecastViewHolder, posstion: Int) {
        forecasts[posstion]
            .also { viewHolder.bind(it) }

    }

    class ForecastViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(forecast: Forecast) {
            with(view) {
                forecast.dateText
                    ?.split(" ")
                    ?.also { txt_date.text = it[0] }
                    ?.also { txt_time.text = buildTimeString(it[1]) }

                txt_cloudiness.text = forecast.clouds?.cloudiness.toString()
                txt_temperature.text = forecast.details?.temperature.toString()

                txt_Speed.text = forecast.wind?.speed.toString()
                txt_degree.text = forecast.wind?.degree.toString()
            }

        }


        private fun buildTimeString(time: String) = time
            .split(":")
            .let { "${it[0]}:${it[1]}" }


    }
}


//endregion