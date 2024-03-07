package com.azerbaijankiraye.kirayeaz.Profile.ItemProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.azerbaijankiraye.kirayeaz.databinding.FragmentAddBalanceBinding


class AddBalanceFragment : Fragment() {
    lateinit var binding:FragmentAddBalanceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBalanceBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()
    }

    private fun buttons() {
        binding.apply {
            bTo1.setOnClickListener {

            }
            bTo6.setOnClickListener {

            }
            bTo10.setOnClickListener {

            }
        }
    }


}