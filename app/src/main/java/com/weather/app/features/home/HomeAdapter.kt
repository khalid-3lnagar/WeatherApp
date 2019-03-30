package com.weather.app.features.home

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.weather.app.R
import com.weather.entties.City
import com.weather.useecasses.CitiesResult
import kotlinx.android.synthetic.main.item_city.view.*
import java.io.Serializable

const val BROADCAST_ACTION_SHOW_CITY = "BROADCAST_ACTION_SHOW_CITY"
const val INTENT_EXTRA_CITY = "INTENT_EXTRA_CITY"

class HomeAdapter(private val cities: CitiesResult, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<CityViewHolder>() {

    init {
        cities.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    override fun onCreateViewHolder(root: ViewGroup, possition: Int): CityViewHolder {
        return LayoutInflater
            .from(root.context)
            .inflate(R.layout.item_city, root, false)
            .let { CityViewHolder(it) }
    }

    override fun onBindViewHolder(cityViewHolder: CityViewHolder, possition: Int) {
        cityViewHolder
            .bind(cities.value!![possition])
    }

    override fun getItemCount(): Int = cities.value?.size ?: 0


}

class CityViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val cityNameTxt by lazy { view.city_name_txt }
    private val cityCountryTxt by lazy { view.city_country_txt }
    private val cityLatitudeTxt by lazy { view.city_latitude_txt }
    private val cityLongitudeTat by lazy { view.city_longitude_txt }
    private val showCityBtn by lazy { view.show_city }

    fun bind(city: City) {
        with(city) {
            cityNameTxt.text = name
            cityCountryTxt.text = country
            cityLatitudeTxt.text = coordinates?.latitude.toString()
            cityLongitudeTat.text = coordinates?.longitude.toString()
        }
        showCityBtn.setOnClickListener { sendShowCityBroadcast(city) }

    }

    private fun sendShowCityBroadcast(city: City) {
        Intent(BROADCAST_ACTION_SHOW_CITY)
            .putExtra(INTENT_EXTRA_CITY, city as Serializable)
            .also { view.context.sendBroadcast(it) }
    }
}