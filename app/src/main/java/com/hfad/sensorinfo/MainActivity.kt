package com.hfad.sensorinfo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.FileOutputStream
import java.io.IOException

const val REQUEST_SELECT_URI = 100

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
                viewModel.selected = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spnColor.onItemSelectedListener = spnColorListener
    }

    override fun onItemClick(view: View?, position: Int) {
        Toast.makeText(this, "position: $position", Toast. LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        applyPreferences()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuPref -> {
                val settingsActivity = Intent(this,SettingsActivity::class.java)
                startActivity(settingsActivity)
                true
            }
            R.id.menuSave -> {
                val selectLocation = Intent(Intent.ACTION_CREATE_DOCUMENT)
                selectLocation.setType("text/plain") //mime-тип файла
                startActivityForResult(selectLocation, REQUEST_SELECT_URI)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_URI && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                Thread(Runnable {
                    saveFile(uri)
                }).start()
            }
        }
    }

    private fun saveFile(uri: Uri) = try {
        contentResolver.openFileDescriptor(uri,"w")?.let { pfd ->
            FileOutputStream(pfd.fileDescriptor).writer().use { out ->
                viewModel.sensorsList.forEach { item -> out.write("${item.first}, ${item.second}\n")
                }
            }
            pfd.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    private fun applyPreferences() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        viewModel.setSelected(pref.getString("selected", viewModel.selected.toString()))
        spnColor.setSelection(viewModel.selected)
    }
}