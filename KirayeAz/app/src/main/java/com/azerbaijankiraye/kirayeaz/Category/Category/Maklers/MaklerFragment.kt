package com.azerbaijankiraye.kirayeaz.Category.Category.Maklers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.azerbaijankiraye.kirayeaz.databinding.FragmentMaklersBinding

class MaklerFragment : Fragment() {
    private lateinit var binding: FragmentMaklersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaklersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()

    }

    private fun buttons() {
        binding.bArenda.setOnClickListener {
            findNavController().navigate(com.azerbaijankiraye.kirayeaz.R.id.action_maklersFragment_to_arendaMaklersFragment)
        }
        binding.bSend.setOnClickListener {
            findNavController().navigate(com.azerbaijankiraye.kirayeaz.R.id.action_maklersFragment_to_sendMaklersFragment)
        }
        binding.bBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}