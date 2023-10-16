package com.kirayeazerbaijan.kirayeaz.Caategory.Category.ObjectSendBuy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kirayeazerbaijan.kirayeaz.R
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentObjectBinding


class ObjectFragment : Fragment() {
    private lateinit var binding: FragmentObjectBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObjectBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button()
    }

    private fun button() {
        binding.apply {
            bAllSend.setOnClickListener {
                findNavController().navigate(R.id.action_objectFragment_to_allObjectFragment)
            }
            bKvartira.setOnClickListener {
                findNavController().navigate(R.id.action_objectFragment_to_objectArendFragment)
            }
            bHomeHouse.setOnClickListener {
                findNavController().navigate(R.id.action_objectFragment_to_objectSendFragment)
            }
        }
    }
}