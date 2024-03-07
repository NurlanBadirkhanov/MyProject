package com.azerbaijankiraye.kirayeaz.Profile.SettingsScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.azerbaijankiraye.kirayeaz.LanguageManager
import com.azerbaijankiraye.kirayeaz.Profile.Firebase.FirebaseActivity
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var auth: FirebaseAuth
    private val languageManager by lazy { LanguageManager(requireContext()) }
    private var originalStatusBarColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black_overlay)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        languageManager.saveLanguage(languageManager.getSavedLanguage())
    }
    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor
    }


    override fun onResume() {
        super.onResume()
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black_overlay)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.imageView9.setOnClickListener {
            findNavController().popBackStack()
        }

        val currentUser = auth.currentUser
        buttons()
        language()

        if (currentUser == null) {
            startActivity(Intent(requireContext(), FirebaseActivity::class.java))
            activity!!.finish()
        }
    }

    private fun language() {
        binding.bLanguage.setOnClickListener {
            val currentLanguage = languageManager.getSavedLanguage()
            val newLanguage = if (currentLanguage == "ru") "az" else "ru"
            languageManager.saveLanguage(newLanguage)
            languageManager.setLocale()
            requireActivity().recreate()
            // Обновите текст кнопки в зависимости от текущего языка
            val buttonText = if (newLanguage == "ru")
                "Выбрать язык: Русский"
            else "Dil seçin: Azərbaycanca"
            binding.tvLanguage.text = buttonText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Save language when the view is destroyed
        languageManager.saveLanguage(languageManager.getSavedLanguage())
    }




    private fun buttons() {
        binding.apply {
            bSignOut.setOnClickListener {
                auth.signOut()
                startActivity(Intent(requireContext(), FirebaseActivity::class.java))
                activity!!.finish()
            }
            bToWe.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_weFragment)
            }
            polycy.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_policyFragment)
            }
            bRulesToApp.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_rulesFragment)
            }

        }
    }


}


