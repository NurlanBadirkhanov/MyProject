package com.creatives.vakansiyaaz.ADD

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentAddBinding
import com.creatives.vakansiyaaz.home.navigate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddCategoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()
    }

    private fun buttons() {
        binding.bAdd.setOnClickListener {
            navigate(R.id.addVacancyFragment)
            dismiss()
        }
        binding.bAddPro.setOnClickListener {
            navigate(R.id.addVacancyProFragment)
            dismiss()
        }
        binding.bAddLink.setOnClickListener {
            navigate(R.id.addLinkFragment)
            dismiss()
        }
    }


}