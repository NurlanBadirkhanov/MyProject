package com.ilnodstudio.ansartelecom.screen.list.elektronics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ilnodstudio.ansartelecom.MAIN
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentElectronicListBinding


class ElectronicListFragment : Fragment() {
    lateinit var binding:FragmentElectronicListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectronicListBinding.inflate(layoutInflater,container,false)
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
            bMob.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_electronicListFragment_to_smartPhonFragment)
            }
            bListToClockFrag.setOnClickListener{
                val navController = findNavController()
                navController.navigate(R.id.action_electronicListFragment_to_clockFragment)
            }

            bToTabFrag.setOnClickListener{
                val navController = findNavController()
                navController.navigate(R.id.action_electronicListFragment_to_tabFragment)
            }
            bToNoutBook.setOnClickListener{
                val navController = findNavController()
                navController.navigate(R.id.action_electronicListFragment_to_noutFragment)
            }
        }
    }


}