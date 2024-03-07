package com.creatives.vakansiyaaz.ADD.Spinners

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpheraAdapter
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.databinding.FragmentSferaWorkBinding

class SpheraWorkFragment : Fragment() {
    private lateinit var binding: FragmentSferaWorkBinding
    private lateinit var viewModel: SpinnerViewModel
    private lateinit var adapter: SpheraAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSferaWorkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        initRc()
        search()

    }

        private fun search() {
            val searchView = binding.searchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filterList(newText.orEmpty())
                    return true
                }
            })
        }


    private fun initRc() {
        adapter = SpheraAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        adapter.setData(androidApps)
        adapter.onItemClick = { adminData ->
            viewModel.spheraWork.value = adminData
            findNavController().popBackStack()
        }
    }


    private val androidApps = listOf(
        "Путеводитель по городу,Туризм",
        "Фитнес трекер",
        "Спорт и здоровье",
        "Фото-редактор",
        "Дизайн",
        "Продажи",
        "Финансы",
        "Юриспруденция",
        "Административный персонал",
        "Медицина и фармацевтика",
        "Образование и наука",
        "Охрана и Безопасность",
        "Домашний персонал и уборка",
        "Строительство и ремонт",
        "Информационные технологии, телеком",
        "Промышленность,производтсво",
        "Рестораны, Туризм",
        "Автобизнес,сервисное обслуживание",
        "Красота,Фитнесс",
        "Спорт",
        "Транспорт,Логистика,Склад",
        "Маркетинг,Реклама,PR",
        "HR,Кадры"
    )


}