package com.azerbaijankiraye.kirayeaz.Category.Category.Students

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentStudentsBinding

class StudentsFragment : Fragment() {


    private lateinit var viewModel: StudentsViewModel
    private lateinit var binding:FragmentStudentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentStudentsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()
    }

    private fun buttons() {
        binding.apply {
            bAllArenda.setOnClickListener {
                findNavController().navigate(R.id.action_studentsFragment_to_studentsAllFragment)
            }
            bGiveHome.setOnClickListener {
                findNavController().navigate(R.id.action_studentsFragment_to_giveStudFragment)
            }
            bSearchStud.setOnClickListener {
                findNavController().navigate(R.id.action_studentsFragment_to_searchFragment)
            }
            bBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[StudentsViewModel::class.java]
    }

}