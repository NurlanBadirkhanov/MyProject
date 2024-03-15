package com.ilnodstudio.ansartelecom.screen.list.accessuar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ilnodstudio.ansartelecom.MAIN
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentAccessuarListBinding

class AccessuarListFragment : Fragment() {
    lateinit var binding:FragmentAccessuarListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccessuarListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MAIN.binding.openMenu.visibility = View.GONE
        binding.bexitElek.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
            MAIN.binding.openMenu.visibility = View.VISIBLE
        }
        binding.apply {
            bToToPowerBank.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_powerBankFragment)
            }
            bToCase.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_caseFragment)
            }
            bListToAdapters.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_adaptersFragment)
            }
            bListToFlash.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_flashFragment)
            }
            bListToOther.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_otherFragment)
            }
            bListToUsb.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_usbFragment)
            }
            bListToHeadPhones.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_headphonesFragment)
            }
            bListToVinil.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_vinilFragment)
            }
            bListToGlass.setOnClickListener {
                val navController = findNavController()
                navController.navigate(R.id.action_accessuarListFragment_to_glassFragment)
            }

        }

    }

}