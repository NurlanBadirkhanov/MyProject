package com.azerbaijankiraye.kirayeaz.Category

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel
    private var backPressedOnce = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        buttonTo()
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedOnce) {
                requireActivity().finish()
            } else {
                backPressedOnce = true
                Toast.makeText(requireContext(), getString(R.string.back), Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed(
                    { backPressedOnce = false },
                    2000
                ) // Сброс флага через 2 секунды
            }
        }
        callback.isEnabled = true
    }

    private fun buttonTo() {
        binding.apply {
            bToArendaKvartir.setOnClickListener {
                findNavController().navigate(R.id.action_action_category_to_arendFragment)
            }
            bToObject.setOnClickListener {
                findNavController().navigate(R.id.action_action_category_to_objectFragment)

            }
            bToWord.setOnClickListener {
                findNavController().navigate(R.id.action_action_category_to_wordSendFragment)

            }
            bToSentHome.setOnClickListener {
                findNavController().navigate(R.id.action_action_category_to_sendHomeFragment)
            }
            bToStudents.setOnClickListener {
                findNavController().navigate(R.id.action_action_category_to_studentsFragment)
            }
            bToMaklers.setOnClickListener {
                findNavController().navigate(R.id.action_action_category_to_maklersFragment)
            }

        }
    }

}