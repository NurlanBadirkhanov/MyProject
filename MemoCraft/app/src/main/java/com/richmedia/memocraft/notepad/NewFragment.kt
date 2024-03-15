package com.richmedia.memocraft.notepad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.richmedia.memocraft.databinding.FragmentNewBinding

class NewFragment : Fragment() {
    lateinit var binding: FragmentNewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}