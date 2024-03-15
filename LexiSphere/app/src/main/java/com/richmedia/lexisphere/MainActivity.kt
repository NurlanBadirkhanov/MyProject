package com.richmedia.lexisphere

import DefinitionResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.richmedia.lexisphere.databinding.ActivityMainBinding
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Ничего не делаем здесь, обработчик будет вызван при нажатии на кнопку поиска
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Можно реагировать на изменения текста, если это необходимо
                return false
            }
        })

        binding.button.setOnClickListener {
            val query = binding.searchView.query.toString()
            volley(query)
        }
    }

    private fun volley(searchQuery: String) {
        // Проверка на пустой ввод
        if (searchQuery.isBlank()) {
            // Выводите сообщение об ошибке или предпринимайте другие действия по вашему усмотрению
            return
        }

        val API = "dict.1.1.20240314T055436Z.6397349c4fc0659f.c74cfd5b4d00a1e1e051f24af0c205d2ecbddcc4"
        val lang = "ru-ru"
        val encodedQuery = URLEncoder.encode(searchQuery, "UTF-8")
        val url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=$API&lang=$lang&text=$encodedQuery"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Разбор JSON-ответа с помощью Gson
                val gson = Gson()
                val definitionResponse = gson.fromJson(response, DefinitionResponse::class.java)

                // Создание Intent для перехода на следующую активити
                val intent = Intent(this, NextActivity::class.java)

                // Добавление данных в Intent
                intent.putExtra("word", definitionResponse.definitions[0].text)
                intent.putExtra("partOfSpeech", definitionResponse.definitions[0].pos)
                // Передача списка синонимов, если они есть
                val synonymsList = definitionResponse.definitions[0].tr.flatMap { it.syn.orEmpty() }.map { it.text }
                intent.putStringArrayListExtra("synonyms", ArrayList(synonymsList))
                Log.d("MyLog", response.toString())
                startActivity(intent)

            },
            { error ->
                // Обрабатываем ошибку
                Log.e("MyLog", "Error occurred", error)
            })

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }





}
