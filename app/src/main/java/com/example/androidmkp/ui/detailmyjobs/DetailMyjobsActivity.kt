/**
 * Activity yang menampilkan detail dari masing-masing My Jobs ketika di klik.
 */

package com.example.androidmkp.ui.detailmyjobs

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmkp.databinding.ActivityDetailMyjobsBinding
import com.example.androidmkp.model.Proyek
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class DetailMyjobsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMyjobsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyjobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val proyekData = intent.getParcelableExtra<Proyek>("proyekData")

        proyekData?.let {
            binding.proyekName.text = it.proyek
            binding.kode.text = it.kode
            binding.penyelenggara.text = it.penyelenggara
            binding.lokasi.text = it.lokasi
            binding.kota.text = it.kota
            binding.slot.text = "${it.jumlahpekerja}/${it.totalslot}"
            binding.deskripsi.text = it.deskripsi
            binding.durasi.text = it.durasi
            val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val startDate = inputFormat.parse(it.start)
            val endDate = inputFormat.parse(it.end)

            binding.tanggalmulai.text = startDate?.let { outputFormat.format(it) } ?: it.start
            binding.tanggalakhir.text = endDate?.let { outputFormat.format(it) } ?: it.end
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}
