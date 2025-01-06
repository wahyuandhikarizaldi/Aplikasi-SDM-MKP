// MyJobsFragment menghandle navigasi dari Tab Layout dari Active Jobs dan Past Jobs

package com.example.androidmkp.ui.myjobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.androidmkp.databinding.FragmentMyjobsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyJobsFragment : Fragment() {

    private lateinit var binding: FragmentMyjobsBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyjobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Active Jobs"
                1 -> "Past Jobs"
                else -> "Active Jobs"
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }
}
