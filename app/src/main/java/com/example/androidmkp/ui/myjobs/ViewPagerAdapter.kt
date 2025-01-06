// Naviasi dari ActiveJobsFragment dan PastJobsFragment

package com.example.androidmkp.ui.myjobs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveJobsFragment()
            1 -> PastJobsFragment()
            else -> ActiveJobsFragment()
        }
    }
}
