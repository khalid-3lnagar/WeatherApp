package com.weather.app.features.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.weather.app.R
import com.weather.entties.City
import kotlinx.android.synthetic.main.fragment_home.*

class HomeScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class HomeFragment : Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieving
            .observe(this, Observer { searchProgressBar.visibility = if (it!!) View.VISIBLE else View.GONE })
        viewModel.result.observe(this, resultObserver())

        search_button.setOnClickListener { viewModel.onSearchButtonClicked(searchEditText.text.toString()) }
    }

    private fun resultObserver(): Observer<List<City>> {
        return Observer {
            Toast.makeText(
                context, "database has ${it?.size} Cities of ${searchEditText.text} ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
