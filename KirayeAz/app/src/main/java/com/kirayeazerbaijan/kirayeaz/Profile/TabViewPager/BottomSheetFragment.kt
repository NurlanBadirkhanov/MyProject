package com.kirayeazerbaijan.kirayeaz.Profile.TabViewPager


import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirayeazerbaijan.kirayeaz.AllAdapter
import com.kirayeazerbaijan.kirayeaz.AllData
import com.kirayeazerbaijan.kirayeaz.R
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentBottomSheetBinding

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentBottomSheetBinding
    private lateinit var adapter: AllAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AllAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter

        val list = listOf(
            AllData("Qusar",100,"Nurlan", R.drawable.buy),
            AllData("baku",100,"Nurlan", R.drawable.buy),
            AllData("kuba",100,"Nurlan", R.drawable.buy),
            AllData("xacmaz",100,"Nurlan", R.drawable.buy),
            AllData("Qusar",100,"Nurlan", R.drawable.buy),
            AllData("Qusar",100,"Nurlan", R.drawable.buy),
            AllData("Qusar",100,"Nurlan", R.drawable.buy),
            )
        adapter.setData(list)
    }
}
