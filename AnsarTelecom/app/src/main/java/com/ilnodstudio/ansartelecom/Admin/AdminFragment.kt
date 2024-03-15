package com.ilnodstudio.ansartelecom.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ilnodstudio.ansartelecom.R
import com.ilnodstudio.ansartelecom.databinding.FragmentAdminBinding


class AdminFragment : Fragment() {
    lateinit var binding: FragmentAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b()
    }
    private fun b() {
        binding.button.setOnClickListener {
            val login = binding.emailEt1.text.toString()
            val password = binding.passET2.text.toString()

            if (login == "MIKALEZGIN1" && password == "MIKA038") {
                val navController = findNavController()
                navController.navigate(R.id.action_adminFragment_to_adminListFragment)
            }else{
                Toast.makeText(requireContext(),"Не пытайтесь!",Toast.LENGTH_SHORT).show()
            }
        }

    }
}

