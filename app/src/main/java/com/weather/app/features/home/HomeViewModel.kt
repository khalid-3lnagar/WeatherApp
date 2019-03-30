package com.weather.app.features.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weather.entties.City
import com.weather.useecasses.CitiesResult
import com.weather.useecasses.RetrieveCityByName
import com.weather.useecasses.engine.toMutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.Serializable

class HomeViewModel(
    val showCity: PublishSubject<Serializable> = PublishSubject.create(),
    val retrieving: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val citiesResult: CitiesResult = ArrayList<City>().toMutableLiveData(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val retrieveCityByName: RetrieveCityByName = RetrieveCityByName(retrieving, citiesResult)
) : ViewModel() {
    fun onSearchButtonClicked(cityName: String?) {

        Observable.fromCallable { retrieveCityByName(cityName) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
            .also { disposables.add(it) }

    }

    override fun onCleared() {
        super.onCleared()
        showCity.onComplete()
        disposables.dispose()

    }

}