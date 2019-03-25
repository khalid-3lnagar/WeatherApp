package com.weather.app.features.randomaizer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.weather.app.R
import kotlinx.android.synthetic.main.activity_ranomizer.*

class RandomizeActivity : FragmentActivity() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RandomNumberViewModel::class.java)
            .apply {
                numberLiveData.observe(this@RandomizeActivity, Observer { randomNumberTxt.text = it.toString() })
                clickedState.observe(this@RandomizeActivity, Observer { clickedStateBtn.text = it })
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranomizer)

        incrementBtn.setOnClickListener { viewModel.incrementNumber() }
        clickedStateBtn.setOnClickListener { viewModel.changeClickedState() }
    }
}
