package com.richmedia.ideapad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.richmedia.ideapad.data.DataStore
import com.richmedia.ideapad.data.Note
import com.richmedia.ideapad.databinding.ActivityNewNoteBinding

import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding

    private var isNew = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var note: Note? = null
        if(intent.extras!=null) {
            note = intent.extras!!.get("note") as Note?
            if(note!=null) {
                isNew = false
                binding.titleET.setText(note.title)
                binding.textET.setText(note.text)
            }

        }
        binding.backIV.setOnClickListener{
            super.onBackPressed()
        }

        binding.doneIV.setOnClickListener {
            println("testing isnew $isNew")
            if (isNew) {
                save()
            } else {
                if (note != null) {
                    update(note)
                }
            }
        }
    }

    private fun save() {
        DataStore.execute(Runnable {
            val note = updateNote()
            DataStore.notes.insert(note)
        })
        finish()
    }

    private fun update(note1: Note){
        DataStore.execute(Runnable {
            DataStore.notes.update(updateNote(note1))
        })
        finish()
    }

    private fun updateNote(): Note  {
        val note = Note()
        note.title = binding.titleET.text.toString()
        note.text = binding.textET.text.toString()
        note.updatedAt = Date()
        return note
    }

    private fun updateNote(note :Note): Note  {
        note.title = binding.titleET.text.toString()
        note.text = binding.textET.text.toString()
        note.updatedAt = Date()
        return note
    }
}
