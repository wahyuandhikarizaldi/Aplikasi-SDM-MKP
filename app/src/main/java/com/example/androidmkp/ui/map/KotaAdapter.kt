// KotaAdapter adalah untuk menampilkan daftar kota dalam bentuk list menggunakan RecyclerView di Android.


package com.example.androidmkp.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmkp.R
import com.example.androidmkp.model.Kota

class KotaAdapter(private val kotaList: List<Kota>, private val onItemClick: (Kota) -> Unit) : RecyclerView.Adapter<KotaAdapter.KotaViewHolder>() {

    inner class KotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)

        fun bind(kota: Kota) {
            tvName.text = kota.name
            itemView.setOnClickListener {
                onItemClick(kota)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return KotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: KotaViewHolder, position: Int) {
        holder.bind(kotaList[position])
    }

    override fun getItemCount(): Int = kotaList.size
}

