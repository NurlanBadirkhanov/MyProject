package com.creatives.vakansiyaaz.Profile.ItemProfile.MyAdsItem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.creatives.vakansiyaaz.databinding.FragmentMyAdsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AllMyAdsFragment : Fragment() {

    lateinit var binding:FragmentMyAdsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAdsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.vp2
        tabLayout = binding.tabLayout

        val pagerAdapter = MyAdsViewPager(this)
        viewPager.adapter = pagerAdapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Обычные Объявления"
                1 -> tab.text = "Vip Объявления"

            }
        }.attach()

    }

}
