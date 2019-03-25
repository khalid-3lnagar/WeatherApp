package com.weather.app.features.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weather.entties.City
import com.weather.useecasses.CitiesResult
import com.weather.useecasses.RetrieveCityByName
import com.weather.useecasses.engine.toMutableLiveData
import com.weather.useecasses.reposotereys.CitiesRepository
import com.weather.useecasses.reposotereys.CitiesRepositoryImplemnter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    val retrieving: MutableLiveData<Boolean> = false.toMutableLiveData(),
    val result: CitiesResult = ArrayList<City>().toMutableLiveData(),
    private val repository: CitiesRepository = CitiesRepositoryImplemnter()
) : ViewModel() {

    val retrieveCityByName = RetrieveCityByName(retrieving, repository, result)
    fun onSearchButtonClicked(cityName: String?) {

        Observable.fromCallable { retrieveCityByName(cityName) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()

    }


}