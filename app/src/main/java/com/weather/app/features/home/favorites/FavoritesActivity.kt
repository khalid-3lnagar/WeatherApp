package com.weather.app.features.home.favorites

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.weather.app.R
import com.weather.app.sendShowCityBroadcast
import com.weather.entties.City
import com.weather.useecasses.AddFavoriteCityById
import com.weather.useecasses.CitiesResult
import com.weather.useecasses.RemoveCityFromFavoritesById
import com.weather.useecasses.RetrieveFavoriteCities
import com.weather.useecasses.engine.toMutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.item_city.view.*

class FavoritesActivity : AppCompatActivity() {


    private val viewModel by lazy { ViewModelProviders.of(this).get(FavoritesViewModel::class.java) }

    private val disposables by lazy { CompositeDisposable() }
    private val favoritesAdapter by lazy { FavoritesAdapter(viewModel.favoritesCities, this, onItemClickListener) }
    private val onItemClickListener = object : FavoritesAdapter.OnItemClickListener {
        override fun onClick(cityId: Long) {
            Snackbar.make(
                rv_favorites,
                "You Just Removed City From Favorites",
                Snackbar.LENGTH_INDEFINITE
            ).also { viewModel.removeCityFromFavorites(cityId, it) }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        initModel()
        initRecyclerView()

    }

    private fun initModel() {
        viewModel.apply {
            retrieving.observe(this@FavoritesActivity, Observer { initRetrieving(it) })
            onRetrievingFavoritesIds()
            favoritesCities.observe(this@FavoritesActivity, Observer { showListOfFavoriteCities(it) })
        }
    }

    private fun initRecyclerView() {
        with(rv_favorites) {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoritesAdapter
        }
    }

    private fun showListOfFavoriteCities(cities: List<City>?) {
        if (cities.isNullOrEmpty()) {
            txt_empty_msg.visibility = VISIBLE
            rv_favorites.visibility = INVISIBLE
        } else {
            txt_empty_msg.visibility = INVISIBLE
            rv_favorites.visibility = VISIBLE
            favoritesAdapter.notifyDataSetChanged()
            rv_favorites.adapter = favoritesAdapter


        }

    }


    private fun initRetrieving(it: Boolean?) {
        if (it == true) {
            pb_retrieving_favorites.visibility = VISIBLE
            rv_favorites.visibility = INVISIBLE

        } else {
            pb_retrieving_favorites.visibility = INVISIBLE
            rv_favorites.visibility = VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.dispose()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onRetrievingFavoritesIds()
    }

}

//region ViewModel
class FavoritesViewModel(
    val retrieving: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val favoritesCities: CitiesResult = ArrayList<City>().toMutableLiveData(),
    private val schedulerIo: Scheduler = Schedulers.io(),
    private val mainScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val addFavoriteCityById: AddFavoriteCityById = AddFavoriteCityById(),
    private val removeCityFromFavoritesById: RemoveCityFromFavoritesById = RemoveCityFromFavoritesById(),
    private val retrieveFavoriteCities: RetrieveFavoriteCities = RetrieveFavoriteCities(retrieving, favoritesCities)
) : ViewModel() {

    fun onRetrievingFavoritesIds() {

        Observable
            .fromCallable { retrieveFavoriteCities() }
            .observeOn(mainScheduler)
            .subscribeOn(schedulerIo)
            .subscribe({}, {
                retrieving.postValue(false)
                favoritesCities.postValue(listOf())
            })
            .also { disposables.add(it) }

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun removeCityFromFavorites(cityId: Long, snackbar: Snackbar) {
        Completable.fromCallable { removeCityFromFavoritesById(cityId) }
            .observeOn(mainScheduler)
            .subscribeOn(schedulerIo)
            .subscribe { onSuccess(snackbar, cityId) }
            .also { disposables.add(it) }

    }

    private fun onSuccess(snackbar: Snackbar, cityId: Long) {
        snackbar
            .setAction(R.string.undo_string) { addCityToFavorites(cityId) }
            .show()
        onRetrievingFavoritesIds()
    }

    private fun addCityToFavorites(id: Long) {

        Completable.fromCallable { addFavoriteCityById(id) }
            .subscribeOn(schedulerIo)
            .observeOn(mainScheduler)
            .subscribe { onRetrievingFavoritesIds() }
            .also { disposables.add(it) }

    }


}
//endregion

//region RecyclerViewAdapter
class FavoritesAdapter(
    private val cities: CitiesResult,
    lifecycleOwner: LifecycleOwner,
    private val onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteCityViewHolder>() {

    init {
        cities.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })

    }

    interface OnItemClickListener {
        fun onClick(cityId: Long)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, posstion: Int): FavoriteCityViewHolder {
        LayoutInflater.from(viewGroup.context).inflate(R.layout.item_city, viewGroup, false)
            .also { return FavoriteCityViewHolder(it, onClickListener) }
    }

    override fun onBindViewHolder(viewHolder: FavoriteCityViewHolder, posstion: Int) {

        cities.value
            ?.get(posstion)
            ?.also { viewHolder.onBind(it) }
    }

    override fun getItemCount(): Int = cities.value?.size ?: 0

    class FavoriteCityViewHolder(private val view: View, private val onClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {

        fun onBind(city: City) {
            with(city) {
                view.txt_city_name.text = name
                view.txt_city_country.text = country
                view.txt_city_latitude.text = coordinates?.latitude.toString()
                view.txt_city_longitude.text = coordinates?.longitude.toString()

            }
            view.btn_show_city.setOnClickListener { sendShowCityBroadcast(city, view) }
            view.fab_item_favorite.show()
            view.fab_item_favorite.setOnClickListener {
                view.visibility = GONE
                onClickListener.onClick(city.id)
            }
        }


    }
}
//endregion