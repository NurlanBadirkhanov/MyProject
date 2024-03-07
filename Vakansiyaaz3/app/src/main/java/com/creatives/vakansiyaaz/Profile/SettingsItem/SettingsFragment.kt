package com.creatives.vakansiyaaz.Profile.SettingsItem

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.creatives.vakansiyaaz.Login.LoginActivity
import com.creatives.vakansiyaaz.Login.Number.NumberActivity
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentSettingsBinding
import com.creatives.vakansiyaaz.home.navigate
import com.creatives.vakansiyaaz.home.popBack
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    private var originalStatusBarColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black_overlay)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons()
    }

    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor
    }


    override fun onResume() {
        super.onResume()
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black_overlay)
    }
    private fun buttons() {
        binding.apply {

            bSignOut.setOnClickListener {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity!!.finish()
            }

            bToWe.setOnClickListener {
                navigate(R.id.weFragment)
            }
            bRulesToApp.setOnClickListener {
                navigate(R.id.rulesFragment)
            }
            polycy.setOnClickListener {
                navigate(R.id.policyFragment)
            }
            bExit.setOnClickListener {
                popBack()
            }
        }
    }


}