package com.hfad.sensorinfo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), SensorRVAdapter.ItemClickListener {
    lateinit var lvSensors : ListView
    lateinit var rvSensors : RecyclerView
    lateinit var spnColor : Spinner

    val viewModel: SensorViewModel by lazy {
        ViewModelProvider(this).get(SensorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //lvSensors = findViewById(R.id.lvSensors)
        spnColor = findViewById(R.id.spnColor)
        rvSensors = findViewById(R.id.rvSensors)

        val dataFrom = arrayOf("VENDOR","NAME")
        val placeTo = intArrayOf(R.id.tvName, R.id.tvVendor)

        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, viewModel.sensorsList)
        //val adapter = SimpleAdapter(this, viewModel.listAsMaps, R.layout.list_item, dataFrom, placeTo)
        val adapter = SensorRVAdapter(this, viewModel.sensorsList as MutableList<SensorItem>)
        adapter.setClickListener(this)

        /*lvSensors.adapter = adapter
        lvSensors.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
        }*/

        rvSensors.layoutManager = LinearLayoutManager(this)
        rvSensors.adapter = adapter

        val spnAdapter = ArrayAdapter.createFromResource(this, R.array.colorsArray, android.R.layout.simple_spinner_item)
        spnColor.adapter = spnAdapter
        val spnColorListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> rvSensors.setBackgroundColor(Color.WHITE)
                    1 -> rvSensors.setBackgroundColor(Color.BLUE)
                    2 -> rvSensors.setBackgroundColor(Color.YELLOW)
                    3 -> rvSensors.setBackgroundColor(Color.GREEN)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spnColor.onItemSelectedListener = spnColorListener
    }

    override fun onItemClick(view: View?, position: Int) {
        Toast.makeText(this, "position: $position", Toast. LENGTH_SHORT).show()
    }
}