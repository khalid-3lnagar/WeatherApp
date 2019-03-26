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
import android.widget.Toast
import com.weather.app.R
import com.weather.entties.City
import kotlinx.android.synthetic.main.fragment_home.*

class HomeScreen : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }
    private val showCityBroadcastReceiver by lazy { ShowCityBroadCastReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        viewModel.retrieving
            .observe(this, Observer { searchProgressBar.visibility = if (it!!) View.VISIBLE else View.GONE })

        home_recyclerView.layoutManager = LinearLayoutManager(this)
        home_recyclerView.adapter = HomeAdapter(viewModel.citiesResult, this)


        search_button.setOnClickListener { viewModel.onSearchButtonClicked(searchEditText.text.toString()) }
        registerReceiver(showCityBroadcastReceiver, IntentFilter(BROADCAST_ACTION_SHOW_CITY))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(showCityBroadcastReceiver)
    }
    private fun resultObserver(): Observer<List<City>> {
        return Observer {
        }
    }
}

class ShowCityBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val cityId = p1
            ?.getSerializableExtra(INTENT_EXTRA_CITY)
            ?.let { it as City }
            ?.id ?: 0L
        Toast.makeText(p0, "$  first broadcast from $cityId ", Toast.LENGTH_SHORT).show()
    }

}