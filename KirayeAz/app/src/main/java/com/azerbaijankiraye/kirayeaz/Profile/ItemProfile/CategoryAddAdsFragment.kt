package com.azerbaijankiraye.kirayeaz.Profile.ItemProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentCategoryAddAdsBinding


class CategoryAddAdsFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentCategoryAddAdsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryAddAdsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()
    }

    private fun buttons() {
        binding.apply {
            bToAds.setOnClickListener {
                findNavController().navigate(R.id.action_action_profile_to_newAdsFragment)
                dismiss()
            }
            bToAdsStudents.setOnClickListener {
                findNavController().navigate(R.id.action_action_profile_to_newAdsStudentsFragment)
                dismiss()
            }
            bToMaklers.setOnClickListener {
                findNavController().navigate(R.id.action_action_profile_to_newAdsMaklerFragment)
                dismiss()
            }
        }
    }


}