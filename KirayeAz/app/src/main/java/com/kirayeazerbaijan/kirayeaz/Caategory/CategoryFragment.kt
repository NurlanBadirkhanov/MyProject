package com.kirayeazerbaijan.kirayeaz.Caategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kirayeazerbaijan.kirayeaz.R
import com.kirayeazerbaijan.kirayeaz.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

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
        }
    }

}