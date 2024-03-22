package com.richmedia.ideapad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.richmedia.ideapad.adapter.NotesAdapter
import com.richmedia.ideapad.databinding.ActivityHomeBinding
import com.richmedia.ideapad.databinding.ActivityMainBinding


class HomeActivity : AppCompatActivity() {
     lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fakeAppbarRL.setOnClickListener{
            startActivity(Intent(this,NewNoteActivity::class.java))
        }

        binding.notesRV.layoutManager = GridLayoutManager(this,2)
        binding.notesRV.adapter = NotesAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    public override fun onDestroy() {
        super.onDestroy()
        binding.notesRV!!.adapter = null
    }

    private fun refresh() {
        (binding.notesRV!!.adapter as NotesAdapter).refresh()
    }


}
