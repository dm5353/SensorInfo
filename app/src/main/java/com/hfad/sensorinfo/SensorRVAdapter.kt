package com.hfad.sensorinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SensorRVAdapter(context: Context?, val data: MutableList<SensorItem>) : RecyclerView.Adapter<SensorRVAdapter.SensorViewHolder?>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var iClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder
    {
        val view: View = layoutInflater.inflate(R.layout.list_item, parent, false)
        return SensorViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val item = data[position]
        holder.nameTextView.text = item.first
        holder.vendorTextView.text = item.second
    }

    inner class SensorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var nameTextView: TextView = itemView.findViewById(R.id.tvName)
        var vendorTextView: TextView = itemView.findViewById(R. id.tvVendor)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            iClickListener?.onItemClick(view, adapterPosition)
        }

    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        iClickListener = itemClickListener
    }
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
