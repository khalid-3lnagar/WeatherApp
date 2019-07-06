package com.weather.app.features.home

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.weather.app.R
import com.weather.app.sendShowCityBroadcast
import com.weather.entties.City
import com.weather.useecasses.CitiesResult
import kotlinx.android.synthetic.main.item_city.view.*

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

    fun bind(city: City) {
        with(city) {
            view.txt_city_name.text = name
            view.txt_city_country.text = country
            view.txt_city_latitude.text = coordinates?.latitude.toString()
            view.txt_city_longitude.text = coordinates?.longitude.toString()
        }
        view.btn_show_city.setOnClickListener { sendShowCityBroadcast(city, view) }

    }

}