package com.waether.app.features.randomaizer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.waether.app.R
import kotlinx.android.synthetic.main.activity_ranomizer.*

class RandomizeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranomizer)
        val viewModel = ViewModelProviders.of(this).get(RandomNumberViewModel::class.java)
        viewModel.randomNumberLiveData.observe(this, Observer { random_number_txt.text = it.toString() })


    }
}
