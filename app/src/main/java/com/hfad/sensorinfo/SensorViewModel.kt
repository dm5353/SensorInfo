package com.hfad.sensorinfo

import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel()  {
    private val repository = SensorRepository.get()
    val sensorsList = repository.sensorList
    val listAsMaps = repository.listAsMaps()
}