package com.weather.useecasses

import android.os.CountDownTimer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


const val FINISH_AFTER_MILLIS = 10000L
const val TIME_ON_TICK = 1000L

class Ticker(private val onTicking: () -> Unit) : CountDownTimer(FINISH_AFTER_MILLIS, TIME_ON_TICK) {

    override fun onTick(timeLeft: Long) {
        onTicking()
    }

    override fun onFinish() {

    }


}

fun rxTicker(onTicking: () -> Unit) = Observable.interval(TIME_ON_TICK, TimeUnit.MILLISECONDS)
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .repeat(FINISH_AFTER_MILLIS)
    .subscribe { onTicking() }!!