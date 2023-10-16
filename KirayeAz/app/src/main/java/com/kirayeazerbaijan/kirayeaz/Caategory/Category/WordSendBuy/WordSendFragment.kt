package com.kirayeazerbaijan.kirayeaz.Caategory.Category.WordSendBuy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kirayeazerbaijan.kirayeaz.R
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentWordSendBinding


class WordSendFragment : Fragment() {
    private lateinit var binding: FragmentWordSendBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordSendBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button()
    }

    private fun button() {
        binding.apply {
            bAllArenda.setOnClickListener {
                findNavController().navigate(R.id.action_wordSendFragment_to_allWordFragment)
            }
            bKvartira.setOnClickListener {
                findNavController().navigate(R.id.action_wordSendFragment_to_wordArendFragment)
            }
            bHomeHouse.setOnClickListener {
                findNavController().navigate(R.id.action_wordSendFragment_to_sendWordFragment)
            }
        }
    }


}