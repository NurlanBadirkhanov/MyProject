package com.richmedia.lexisphere

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        // Получение данных из Intent
        val word = intent.getStringExtra("word")
        val partOfSpeech = intent.getStringExtra("partOfSpeech")
        val synonyms = intent.getStringArrayListExtra("synonyms")

        // Использование полученных данных
        val wordTextView = findViewById<TextView>(R.id.wordTextView)
        wordTextView.text = "Слово: $word\n"
        wordTextView.append("Часть речи: $partOfSpeech\n")

        val synonymsTextView = findViewById<TextView>(R.id.synonymsTextView)
        synonymsTextView.text = "Синонимы:\n"
        synonyms?.forEach { synonym ->
            synonymsTextView.append("$synonym\n")
        }
    }
}
