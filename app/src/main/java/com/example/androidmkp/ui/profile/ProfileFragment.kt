// Menghandle page Profile
// Menampilkan informasi profil pengguna yang sudah login.
// Menyediakan opsi untuk mengedit profil, mengubah kata sandi, dan keluar dari aplikasi.

package com.example.androidmkp.ui.profile

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidmkp.ViewModelFactory
import com.example.androidmkp.databinding.FragmentProfileBinding
import com.example.androidmkp.di.Injection
import com.example.androidmkp.model.UserModel
import com.example.androidmkp.ui.changepassword.ChangePasswordActivity
import com.example.androidmkp.ui.editprofile.EditProfileActivity
import com.example.androidmkp.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity().application as Application)
        viewModel = ViewModelProvider(this@ProfileFragment, factory)[ProfileViewModel::class.java]

        lifecycleScope.launch {
            val userPreference = Injection.provideUserPreference(requireContext())
            val userSession = userPreference.getSession().first()

            if (!userSession.isLogin) {
                navigateToWelcome()
                return@launch
            }

            populateProfileData(userSession)

            Log.d("riwayatproyek", userSession.riwayatproyek)

            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchSpreadsheetData(userSession.nid)
                viewModel.spreadsheetData.observe(viewLifecycleOwner) { data ->
                    if (data.isNotEmpty()) {
                        val rowData = data.first()
                        binding.tvNid.text = rowData.nid
                        binding.tvName.text = rowData.nama
                        binding.tvPhone.text = rowData.phone
                        binding.tvKota.text = "${rowData.kota}, ${rowData.provinsi}"
                        binding.tvPosisi.text = rowData.posisi

                        viewModel.saveSession(UserModel(rowData.nid, rowData.nama, rowData.birthdate, rowData.phone, rowData.posisi, rowData.kota, rowData.provinsi, rowData.password, rowData.riwayatproyek, true))

                    }

                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            viewModel.getStatus().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        setupAction()
    }

    private fun setupAction() {

            binding.btnEdit.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
            binding.arwEdit.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }

        binding.btnPassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.arwPassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

            binding.btnLogout.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.logout()
                    navigateToWelcome()
                }
            }
            binding.arwLogout.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.logout()
                    navigateToWelcome()
                }
            }


        }

    @SuppressLint("SetTextI18n")
    private fun populateProfileData(userSession: UserModel) {
        binding.tvNid.text = userSession.nid
        binding.tvName.text = userSession.nama
        binding.tvPhone.text = userSession.phone
        binding.tvKota.text = "${userSession.kota}, ${userSession.provinsi}"
        binding.tvPosisi.text = userSession.posisi
    }

    private fun navigateToWelcome() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
