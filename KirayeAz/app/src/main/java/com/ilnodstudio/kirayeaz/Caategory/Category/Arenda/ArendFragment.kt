package com.ilnodstudio.kirayeaz.Caategory.Category.Arenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ilnodstudio.kirayeaz.R
import com.ilnodstudio.kirayeaz.databinding.FragmentArendaBinding


class ArendFragment : Fragment() {
    private lateinit var binding:FragmentArendaBinding


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
        }
    }


}