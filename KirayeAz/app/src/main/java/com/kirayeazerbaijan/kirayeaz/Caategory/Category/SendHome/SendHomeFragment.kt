package com.kirayeazerbaijan.kirayeaz.Caategory.Category.SendHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kirayeazerbaijan.kirayeaz.R
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentSendHomeBinding


class SendHomeFragment : Fragment() {
    private lateinit var binding: FragmentSendHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button()
    }

    private fun button() {
        binding.apply {
            bAllSend.setOnClickListener {
                findNavController().navigate(R.id.action_sendHomeFragment_to_allSendFragment)
            }
            bKvartira.setOnClickListener {
                findNavController().navigate(R.id.action_sendHomeFragment_to_sendKvartiraFragment)
            }
            bHomeHouse.setOnClickListener {
                findNavController().navigate(R.id.action_sendHomeFragment_to_sendHomeHouseFragment)
            }
        }
    }


}