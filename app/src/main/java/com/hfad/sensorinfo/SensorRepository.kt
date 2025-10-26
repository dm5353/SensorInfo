package com.hfad.sensorinfo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

typealias SensorItem = Pair<String, String>

class SensorRepository private constructor(context: Context) {
    val sensorList = createSensorsList(context)

    companion object {
        private var INSTANCE: SensorRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE==null) {
                INSTANCE = SensorRepository(context)
            }
        }
        fun get(): SensorRepository {
            return INSTANCE ?: throw IllegalStateException("Repository should be initialized!")
        }
        private fun createSensorsList(context: Context): List<SensorItem> {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
            val result = mutableListOf<SensorItem>()
            sensors.forEach {
                if (it.type > 0) result.add(Pair(it.name,it.vendor))
            }
            return result
        }
    }

    fun listAsMaps(): MutableList<Map<String, String>> {
        val result: MutableList<Map<String, String>> = ArrayList()
        sensorList.forEach {
            val map: MutableMap<String, String> = HashMap()
            map ["VENDOR"] = it.first
            map["NAME"] = it.second
            result.add(map)
        }
        return result
    }
}