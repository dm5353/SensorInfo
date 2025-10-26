package com.hfad.sensorinfo

import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel()  {
    private val repository = SensorRepository.get()
    val sensorsList = repository.sensorList
    val listAsMaps = repository.listAsMaps()
    var selected = 0
        set(value) {
            if (value in 0..3) field = value
        }

    fun setSelected(str: String?) {
        if (!str.isNullOrBlank()) selected = try {
            str.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }
}