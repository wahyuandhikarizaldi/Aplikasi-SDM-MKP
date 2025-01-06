// Activity yang menghandle page pekerjaan aktif atau pekerjaan yang sedang diambil
// ActiveJobsFragment adalah untuk menampilkan daftar proyek yang sedang aktif dan terkait dengan riwayat proyek pengguna yang telah terdaftar.

package com.example.androidmkp.ui.myjobs

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.FragmentActiveJobsBinding
import com.example.androidmkp.di.Injection
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.ui.detailmyjobs.DetailMyjobsActivity
import com.example.androidmkp.ui.proyek.ProyekAdapter
import com.example.androidmkp.ui.proyek.ProyekViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActiveJobsFragment : Fragment() {

    private lateinit var binding: FragmentActiveJobsBinding
    private lateinit var viewModel: ProyekViewModel
    private lateinit var adapter: ProyekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreference = Injection.provideUserPreference(requireContext())
        lifecycleScope.launch {
            val userSession = userPreference.getSession().first()

            var riwayatProyekIds = userSession.riwayatproyek.split(";")

            val factory = ViewModelFactory.getInstance(requireActivity().application as Application)
            viewModel = ViewModelProvider(this@ActiveJobsFragment, factory)[ProyekViewModel::class.java]

            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

            fun updateRecyclerView(riwayatProyekIds: List<String>) {
                viewModel.proyekList.observe(viewLifecycleOwner) { proyekList ->
                    val activeProyekList = proyekList.filter { proyek ->
                        proyek.status == "Active" && riwayatProyekIds.contains(proyek.kode)
                    }

                    if (activeProyekList.isNotEmpty()) {
                        adapter = ProyekAdapter(activeProyekList) { proyek ->
                            val intent = Intent(requireContext(), DetailMyjobsActivity::class.java)
                            intent.putExtra("proyekData", proyek)
                            startActivity(intent)
                        }
                        binding.recyclerView.adapter = adapter
                        binding.progressBar.visibility = View.GONE
                        binding.emptyTextView.visibility = View.GONE
                    } else {
                        binding.emptyTextView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

            updateRecyclerView(riwayatProyekIds)
            viewModel.fetchProyekData()
            binding.progressBar.visibility = View.VISIBLE

            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchProyekData()
                viewModel.fetchSpreadsheetData(userSession.nid)
                viewModel.spreadsheetData.observe(viewLifecycleOwner) { data ->
                    if (data.isNotEmpty()) {
                        val rowData = data.first()

                        val updatedUserModel = UserModel(
                            rowData.nid,
                            rowData.nama,
                            rowData.birthdate,
                            rowData.phone,
                            rowData.posisi,
                            rowData.kota,
                            rowData.provinsi,
                            rowData.password,
                            rowData.riwayatproyek,
                            true
                        )
                        viewModel.saveSession(updatedUserModel)

                        riwayatProyekIds = updatedUserModel.riwayatproyek.split(";")
                        updateRecyclerView(riwayatProyekIds)
                    }
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            viewModel.getStatus().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
