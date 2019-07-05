package com.weather.app.features.home.favorites

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.weather.app.R
import com.weather.entties.City
import com.weather.useecasses.CitiesResult
import com.weather.useecasses.RetrieveFavoriteCities
import com.weather.useecasses.engine.toMutableLiveData
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
    }
}

class FavoritesViewModel(
    val retrieving: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val isFavoritesEmpty: MutableLiveData<Boolean> = true.toMutableLiveData(),
    val favoritesResult: CitiesResult = ArrayList<City>().toMutableLiveData(),
    private val schedulerIo: Scheduler = Schedulers.io(),
    private val mainScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val retrieveCitiesByIds: RetrieveFavoriteCities = RetrieveFavoriteCities(retrieving)

) : ViewModel() {

    fun onRetrievingFavorites() {

        Observable
            .fromCallable { retrieveCitiesByIds(favoritesResult) }
            .observeOn(mainScheduler)
            .subscribeOn(schedulerIo)
            .subscribe(
                { isFavoritesEmpty.postValue(favoritesResult.value.isNullOrEmpty()) },
                { isFavoritesEmpty.postValue(true) })
            .also { disposables.add(it) }


    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}