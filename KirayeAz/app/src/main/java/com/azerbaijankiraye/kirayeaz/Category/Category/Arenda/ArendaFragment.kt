package com.azerbaijankiraye.kirayeaz.Category.Category.Arenda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentArendaBinding

class ArendaFragment : Fragment() {
    private lateinit var binding: FragmentArendaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArendaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button()
    }

    private fun button() {
        binding.apply {
            bAllArenda.setOnClickListener {
                findNavController().navigate(R.id.action_arendFragment_to_allArendFragment)
            }
            bKvartira.setOnClickListener {
                findNavController().navigate(R.id.action_arendFragment_to_kvartiraFragment)
            }
            bHomeHouse.setOnClickListener {
                findNavController().navigate(R.id.action_arendFragment_to_homeHouseFragment)
            }
            bBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}