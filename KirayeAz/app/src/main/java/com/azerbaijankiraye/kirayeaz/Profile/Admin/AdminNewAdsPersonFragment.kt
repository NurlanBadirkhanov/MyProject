package com.azerbaijankiraye.kirayeaz.Profile.Admin


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.azerbaijankiraye.kirayeaz.AllAdapter
import com.azerbaijankiraye.kirayeaz.Profile.Admin.Details.MaklersStudentsSimpleViewPager

import com.azerbaijankiraye.kirayeaz.databinding.FragmentAdminNewAdsPersonBinding

class AdminNewAdsPersonFragment : Fragment() {
    private lateinit var binding:FragmentAdminNewAdsPersonBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminNewAdsPersonBinding.inflate(layoutInflater,container,false)
        val adapter = AllAdapter(requireContext())
        adapter.notifyDataSetChanged()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.vp
        tabLayout = binding.tabLayout
        val pagerAdapter = MaklersStudentsSimpleViewPager(this)
        viewPager.adapter = pagerAdapter
        val adapter = AllAdapter(requireContext())
        adapter.notifyDataSetChanged()


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Обычный"
                1 -> tab.text = "Для Маклеров"
                2 -> tab.text = "Для Студентов"

            }
        }.attach()
    }


}