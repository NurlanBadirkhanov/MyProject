package com.azerbaijankiraye.kirayeaz.Home.AdsPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azerbaijankiraye.kirayeaz.R

class SlideshowPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

     val fragments = listOf(
        SlideshowFragment.newInstance(imageResId = R.drawable.bannerads, position = 0),
        SlideshowFragment.newInstance(imageResId = R.drawable.banner_students, position = 1),
        SlideshowFragment.newInstance(imageResId = R.drawable.banner_we, position = 2),

    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
