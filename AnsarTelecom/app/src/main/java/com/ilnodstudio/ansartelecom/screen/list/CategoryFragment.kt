package com.ilnodstudio.ansartelecom.screen.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {
    lateinit var binding: FragmentCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b()
    }



    private fun b() {
        binding.bElek.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_list_to_electronicListFragment)
        }
        binding.bAccessuar.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_list_to_accessuarListFragment)
        }
        binding.Sim.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_list_to_SIMListFragment2)
        }
    }

}