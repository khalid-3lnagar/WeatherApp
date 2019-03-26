package com.weather.app.features.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.weather.app.R
import com.weather.app.features.forecast.ForecastActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.Serializable
import java.util.concurrent.TimeUnit

class HomeScreen : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }

    private val showCityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.showCity.onNext(intent?.getSerializableExtra(INTENT_EXTRA_CITY)!!)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        viewModel.retrieving
            .observe(this, Observer { searchProgressBar.visibility = if (it!!) View.VISIBLE else View.GONE })

        home_recyclerView.layoutManager = LinearLayoutManager(this)
        home_recyclerView.adapter = HomeAdapter(viewModel.citiesResult, this)

        search_button.setOnClickListener { viewModel.onSearchButtonClicked(searchEditText.text.toString()) }

        viewModel.showCity
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { startForeCast(it) }
            .also { disposables.add(it) }

        registerReceiver(showCityBroadcastReceiver, IntentFilter(BROADCAST_ACTION_SHOW_CITY))
    }

    private fun startForeCast(it: Serializable?) {
        Intent(this@HomeScreen, ForecastActivity::class.java)
            .apply { putExtra(INTENT_EXTRA_CITY, it) }
            .also { startActivity(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(showCityBroadcastReceiver)
        disposables.dispose()
    }


}
