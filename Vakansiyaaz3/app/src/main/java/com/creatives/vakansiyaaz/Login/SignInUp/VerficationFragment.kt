package com.creatives.vakansiyaaz.Login.SignInUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.creatives.vakansiyaaz.databinding.FragmentVerficationBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Flow

class VerficationFragment : Fragment() {
    private lateinit var binding: FragmentVerficationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerficationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bOk.setOnClickListener {
            val auth = FirebaseAuth.getInstance()

            activity!!.finish()
        }

    }


}

