// Menghandle page proyek
// Menampilkan daftar proyek yang memiliki status "Active".
// Memungkinkan pengguna untuk melihat detail proyek dengan mengklik proyek yang ada dalam daftar.

package com.example.androidmkp.ui.proyek

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.FragmentProyekBinding
import com.example.androidmkp.ui.detailproyek.DetailProyekActivity

class ProyekFragment : Fragment() {

    private lateinit var binding: FragmentProyekBinding
    private lateinit var viewModel: ProyekViewModel
    private lateinit var adapter: ProyekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProyekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity().application as Application)
        viewModel = ViewModelProvider(this@ProyekFragment, factory)[ProyekViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.proyekList.observe(viewLifecycleOwner) { proyekList ->
            if (proyekList != null) {
                val activeProyekList = proyekList.filter { it.status == "Active" }

                adapter = ProyekAdapter(activeProyekList) { proyek ->
                    val intent = Intent(requireContext(), DetailProyekActivity::class.java)
                    intent.putExtra("proyekData", proyek)
                    startActivity(intent)
                }
                binding.recyclerView.adapter = adapter
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.fetchProyekData()
        binding.progressBar.visibility = View.VISIBLE

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchProyekData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}

