package com.ilnodstudio.ansartelecom.screen.list.sim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ilnodstudio.ansartelecom.MAIN
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentElectronicListBinding
import com.ilnodstudio.ansartelecom.databinding.FragmentSIMBinding


class SIMListFragment : Fragment() {
    lateinit var binding:FragmentSIMBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSIMBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b()
        MAIN.binding.openMenu.visibility = View.GONE

    }

    private fun b() {
        binding.apply {
            bexitElek.setOnClickListener {
                val navController = findNavController()
                navController.popBackStack()
                MAIN.binding.openMenu.visibility = View.VISIBLE

            }
            bToNar.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_SIMListFragment2_to_narFragment)
            }
            bToToAzercell.setOnClickListener{
                val navController = findNavController()
                navController.navigate(R.id.action_SIMListFragment2_to_azercellFragment)
            }

            bListToBakcell.setOnClickListener{
                val navController = findNavController()
                navController.navigate(R.id.action_SIMListFragment2_to_bakcellFragment)
            }

        }
    }


}