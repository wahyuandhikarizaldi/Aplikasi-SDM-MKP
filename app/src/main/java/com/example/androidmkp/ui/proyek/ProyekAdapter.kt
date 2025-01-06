// ProyekAdapter adalah untuk menampilkan daftar proyek dalam bentuk list menggunakan RecyclerView di Android.


package com.example.androidmkp.ui.proyek

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmkp.R
import com.example.androidmkp.model.Proyek

class ProyekAdapter(
    private val proyekList: List<Proyek>,
    private val onItemClick: (Proyek) -> Unit
) : RecyclerView.Adapter<ProyekAdapter.ProyekViewHolder>() {

    inner class ProyekViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val proyekName: TextView = view.findViewById(R.id.proyekName)
        val penyelenggara: TextView = view.findViewById(R.id.penyelenggara)
        val lokasi: TextView = view.findViewById(R.id.lokasi)
        val kota: TextView = view.findViewById(R.id.kota)
        val slot: TextView = view.findViewById(R.id.slot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProyekViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.proyek_card_item, parent, false)
        return ProyekViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProyekViewHolder, position: Int) {
        val proyek = proyekList[position]
        holder.proyekName.text = proyek.proyek
        holder.penyelenggara.text = proyek.penyelenggara
        holder.lokasi.text = proyek.lokasi
        holder.kota.text = proyek.kota
        holder.slot.text = "${proyek.jumlahpekerja}/${proyek.totalslot}"

        holder.itemView.setOnClickListener {
            onItemClick(proyek)
        }
    }

    override fun getItemCount(): Int = proyekList.size
}
