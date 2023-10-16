package com.kirayeazerbaijan.kirayeaz.Profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kirayeazerbaijan.kirayeaz.R
import com.kirayeazerbaijan.kirayeaz.Profile.TabViewPager.BottomSheetFragment
import com.kirayeazerbaijan.kirayeaz.Profile.TabViewPager.NewAds.NewAdsFragment
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding :FragmentProfileBinding

    private lateinit var viewModel: ProfileViewModel
    var auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        buttons()

    }

    private fun buttons() {
        binding.apply {

        buttonMy.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
        bNewAds.setOnClickListener {
            val bottomSheetFragment = NewAdsFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }
            bSettings.setOnClickListener {
                findNavController().navigate(R.id.action_action_profile_to_settingsFragment)
            }

        }

    }

}