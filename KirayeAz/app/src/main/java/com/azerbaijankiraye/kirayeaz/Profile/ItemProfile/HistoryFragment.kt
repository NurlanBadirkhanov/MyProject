package com.azerbaijankiraye.kirayeaz.Profile.ItemProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.kirayeazerbaijan.kirayeaz.Profile.TabViewPager.NewAds.ItemProfile.HistorAdapter.HistoryAdapter
import com.azerbaijankiraye.kirayeaz.databinding.FragmentHistoryBinding


class HistoryFragment : BottomSheetDialogFragment() {
    lateinit var binding:FragmentHistoryBinding
    lateinit var recyclerView: RecyclerView
//    lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initRc()
//        testAdapter()
    }

//    private fun testAdapter() {
//        val fake = listOf(
//            HistoryData("12.12.2003",12),
//            HistoryData("12.12.200233",125),
//            HistoryData("12.12.202303",125),
//            HistoryData("12.12.20203",152),
//            HistoryData("12.12.20203",172),
//            HistoryData("12.12.20203",172),
//            HistoryData("12.12.20203",132),
//
//
//        )
//        adapter.setData(fake)
//        adapter.notifyDataSetChanged()
//
//    }
//
//    private fun initRc() {
//        adapter = HistoryAdapter(requireContext())
//        recyclerView = binding.rc
//        recyclerView.adapter = adapter
//    }



}