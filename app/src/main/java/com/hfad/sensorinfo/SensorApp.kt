package com.hfad.sensorinfo

import android.app.Application

class SensorApp: Application() {
    override fun onCreate() {
        super.onCreate()
        SensorRepository.initialize(this)
    }
}
