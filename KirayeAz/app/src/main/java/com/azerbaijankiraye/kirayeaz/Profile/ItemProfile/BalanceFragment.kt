package com.azerbaijankiraye.kirayeaz.Profile.ItemProfile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.azerbaijankiraye.kirayeaz.Profile.ItemProfile.HistorAdapter.ViewPagerAdapter
import com.azerbaijankiraye.kirayeaz.R
import com.azerbaijankiraye.kirayeaz.databinding.FragmentBalanceBinding

class BalanceFragment : Fragment() {
    lateinit var binding: FragmentBalanceBinding
    private lateinit var messages: Array<String>
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)
        viewPager = binding.viewPager
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messages = arrayOf(
            getString(R.string.tv_addbalance),
            getString(R.string.tv_history),
            getString(R.string.tv_balance_add1),
        )

        val adapter = ViewPagerAdapter(messages)
        viewPager.adapter = adapter
        binding.imageView36.setOnClickListener {
            findNavController().popBackStack()
        }
        val buttonNext = view.findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            if (viewPager.currentItem < messages.size - 1) {
                viewPager.currentItem += 1
            } else {
                visibleView()
            }
        }
    }

    private fun visibleView() {
        binding.apply {
            viewPager.visibility = View.GONE
            uid.visibility = View.VISIBLE
            uid.text = auth.uid
            buttonNext.visibility = View.INVISIBLE
            btnInsta.visibility = View.VISIBLE
            tv.visibility = View.VISIBLE
            constraint.visibility = View.VISIBLE
            imageView39.visibility = View.VISIBLE
            textView63.visibility = View.VISIBLE
            textView64.visibility = View.VISIBLE
            cons.visibility = View.VISIBLE
            imageView39.setOnClickListener {
                val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("uid", uid.text)
                clipboardManager.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
            }
            btnInsta.setOnClickListener {
                openInstagram()
            }
        }
    }

    private fun openInstagram() {
        val uri = Uri.parse("https://www.instagram.com/kiraya.az/?hl=ru")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        startActivity(intent)
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Подтверждение")
            .setMessage("Хотите завершить?")
            .setPositiveButton("Да") { dialog, which ->
                // Обработка подтверждения, например, возврат на предыдущий экран
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
