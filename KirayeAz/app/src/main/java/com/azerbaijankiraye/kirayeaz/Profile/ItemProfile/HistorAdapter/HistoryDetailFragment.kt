package com.azerbaijankiraye.kirayeaz.Profile.ItemProfile.HistorAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.azerbaijankiraye.kirayeaz.R


class HistoryDetailFragment : Fragment() {
    private var originalStatusBarColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        originalStatusBarColor = activity!!.window.statusBarColor
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue_bytf)
        return inflater.inflate(R.layout.fragment_history_detai, container, false)

    }

    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}