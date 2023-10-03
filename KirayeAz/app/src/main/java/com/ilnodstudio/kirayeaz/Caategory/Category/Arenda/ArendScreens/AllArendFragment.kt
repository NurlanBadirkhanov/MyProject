package com.ilnodstudio.kirayeaz.Caategory.Category.Arenda.ArendScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilnodstudio.kirayeaz.AllAdapter
import com.ilnodstudio.kirayeaz.AllData
import com.ilnodstudio.kirayeaz.R
import com.ilnodstudio.kirayeaz.databinding.FragmentAllArendBinding

class AllArendFragment : Fragment() {
    lateinit var binding:FragmentAllArendBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllArendBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
    }

    private fun initRc() {
        adapter = AllAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter

        val fakeDataList = listOf(
            AllData("Qusar", 100, "Nurlan",R.drawable.kiraye),
            AllData("Baku", 1200, "Android",R.drawable.buy),

            )

        adapter.setData(fakeDataList)
    }


}