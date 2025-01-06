// Fragment yang menghandle Page Peta
// MapFragment adalah untuk menampilkan peta interaktif dengan marker yang menunjukkan lokasi provinsi dan kota, serta menampilkan data terkait tenaga kerja di Indonesia.
// Memperbarui UI dengan data terkait jumlah tenaga kerja di tingkat nasional, provinsi, dan kota.
// Menampilkan daftar provinsi dan kota dalam RecyclerView dan menambahkan interaksi dengan memilih provinsi atau kota untuk melihat data lebih rinci.

package com.example.androidmkp.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmkp.R
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.FragmentMapBinding
import com.example.androidmkp.model.DetailKota
import com.example.androidmkp.model.DetailProvinsi
import com.example.androidmkp.model.Kota
import com.example.androidmkp.model.Provinsi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel: MapViewModel
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapAdapter: MapAdapter

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity().application as Application)
        viewModel = ViewModelProvider(this@MapFragment, factory)[MapViewModel::class.java]

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.totalNasional.observe(viewLifecycleOwner) { totalNasional ->
            mMap.clear()
            binding.progressBar.visibility = View.GONE
            binding.tvTotal.text = "${totalNasional.totalPekerja} Tenaga Kerja"
            binding.tvWelder.text = "${totalNasional.totalWelder}"
            binding.tvFitter.text = "${totalNasional.totalFitter}"
            binding.tvMachinist.text = "${totalNasional.totalMachinist}"
            binding.tvHelper.text = "${totalNasional.totalHelper}"
            binding.tvCity.text = "Indonesia"
            val indonesia = LatLng(-2.5489, 118.0149)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 3.5f))

            setupRecyclerView(totalNasional.provinsi)
            addMarkersToMapProv(totalNasional.provinsi)
        }

        viewModel.detailProvinsi.observe(viewLifecycleOwner) { detailProvinsi ->
            mMap.clear()
            binding.progressBar.visibility = View.GONE
            binding.tvTotal.text = "${detailProvinsi.jumlahPekerja} Tenaga Kerja"
            binding.tvWelder.text = "${detailProvinsi.jumlahWelder}"
            binding.tvFitter.text = "${detailProvinsi.jumlahFitter}"
            binding.tvMachinist.text = "${detailProvinsi.jumlahMachinist}"
            binding.tvHelper.text = "${detailProvinsi.jumlahHelper}"
            binding.tvCity.text = detailProvinsi.name

            displayKota(detailProvinsi.kota)

            addMarkersToMapKota(detailProvinsi.kota)

            zoomToProvinsi(detailProvinsi)
        }

        viewModel.detailKota.observe(viewLifecycleOwner) { detailKota ->
            mMap.clear()
            binding.progressBar.visibility = View.GONE
            binding.tvTotal.text = "${detailKota.jumlahPekerja} Tenaga Kerja"
            binding.tvWelder.text = "${detailKota.jumlahWelder}"
            binding.tvFitter.text = "${detailKota.jumlahFitter}"
            binding.tvMachinist.text = "${detailKota.jumlahMachinist}"
            binding.tvHelper.text = "${detailKota.jumlahHelper}"
            binding.tvCity.text = detailKota.name

            zoomToKota(detailKota)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mMap.clear()
                binding.progressBar.visibility = View.VISIBLE
                viewModel.fetchTotalNasionalData()
            }
        })


    }

    private fun setupRecyclerView(provinsiList: List<Provinsi>) {
        mapAdapter = MapAdapter(provinsiList) { provinsi ->
            viewModel.setLoading(true)
            mMap.clear()

            viewModel.fetchDetailProvinsi(provinsi.nid)

            parentFragmentManager.beginTransaction()
                .addToBackStack("DetailProvinsi")
                .commit()
        }

        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mapAdapter
        }
    }

    private fun zoomToProvinsi(detailProvinsi: DetailProvinsi) {
        val lat = detailProvinsi.latitude.toDouble()
        val lng = detailProvinsi.longitude.toDouble()
        val position = LatLng(lat, lng)

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 7f))
    }

    private fun zoomToKota(detailKota: DetailKota) {
        val lat = detailKota.latitude.toDouble()
        val lng = detailKota.longitude.toDouble()
        val position = LatLng(lat, lng)

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 11f))
    }

    private fun displayKota(kotaList: List<Kota>) {
        val kotaAdapter = KotaAdapter(kotaList) { kota ->
            viewModel.setLoading(true)
            viewModel.fetchDetailKota(kota.nid)
        }
        binding.rvNotes.adapter = kotaAdapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        val indonesia = LatLng(-2.5489, 118.0149)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 3.5f))
    }

    private fun addMarkersToMapProv(provinsiList: List<Provinsi>) {
        for (provinsi in provinsiList) {
            val lat = provinsi.latitude.toDouble()
            val lng = provinsi.longitude.toDouble()
            val position = LatLng(lat, lng)

            mMap.addMarker(MarkerOptions()
                .position(position)
                .title(provinsi.name)
                .snippet("Jumlah Tenaga Kerja: ${provinsi.jumlahPekerja}"))
        }
    }

    private fun addMarkersToMapKota(kotaList: List<Kota>) {
        for (kota in kotaList) {
            val lat = kota.latitude.toDouble()
            val lng = kota.longitude.toDouble()
            val position = LatLng(lat, lng)

            mMap.addMarker(MarkerOptions()
                .position(position)
                .title(kota.name)
                .snippet("Jumlah Tenaga Kerja: ${kota.jumlahPekerja}"))
        }
    }

}