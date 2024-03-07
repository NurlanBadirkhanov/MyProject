package com.azerbaijankiraye.kirayeaz.Profile.Admin.Details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azerbaijankiraye.kirayeaz.Profile.Admin.AdminNewAdsPersonFragment
import com.azerbaijankiraye.kirayeaz.Profile.Admin.AdminScreens.MaklersFragment
import com.azerbaijankiraye.kirayeaz.Profile.Admin.AdminScreens.SimpleAdsFragment
import com.azerbaijankiraye.kirayeaz.Profile.Admin.AdminScreens.StudentsAdminFragment

class MaklersStudentsSimpleViewPager(vp: AdminNewAdsPersonFragment):FragmentStateAdapter(vp) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> SimpleAdsFragment()
            1 -> MaklersFragment()
            2 -> StudentsAdminFragment()
            else -> MaklersFragment()
        }
    }
}