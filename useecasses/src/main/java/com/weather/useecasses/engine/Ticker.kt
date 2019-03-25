package com.weather.useecasses.engine

import android.os.CountDownTimer


const val FINISH_AFTER_MILLIS = 10000L
const val TIME_ON_TICK = 1000L

class Ticker(private val onTicking: () -> Unit) : CountDownTimer(
    FINISH_AFTER_MILLIS,
    TIME_ON_TICK
) {

    override fun onTick(timeLeft: Long) {
        onTicking()
    }

    override fun onFinish() {

    }


}
