package com.azerbaijankiraye.kirayeaz.Profile.SettingsScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentPolicyBinding


class PolicyFragment : Fragment() {
    private lateinit var binding: FragmentPolicyBinding
    private var originalStatusBarColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPolicyBinding.inflate(layoutInflater, container, false)
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.policy)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor
    }


    override fun onResume() {
        super.onResume()
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.policy)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView44.setOnClickListener {
            findNavController().popBackStack()
        }
    }



}