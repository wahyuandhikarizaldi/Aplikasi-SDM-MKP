// MapAdapter adalah untuk menampilkan daftar provinsi dalam bentuk list menggunakan RecyclerView di Android.

package com.example.androidmkp.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmkp.R
import com.example.androidmkp.model.Provinsi

class MapAdapter(
    private val provinsiList: List<Provinsi>,
    private val onItemClick: (Provinsi) -> Unit
) : RecyclerView.Adapter<MapAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)

        fun bind(provinsi: Provinsi) {
            tvName.text = provinsi.name
            itemView.setOnClickListener {
                onItemClick(provinsi)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(provinsiList[position])
    }

    override fun getItemCount(): Int = provinsiList.size
}
