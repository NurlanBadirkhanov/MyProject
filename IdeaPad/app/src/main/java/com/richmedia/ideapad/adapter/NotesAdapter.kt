package com.richmedia.ideapad.adapter


import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.richmedia.ideapad.NewNoteActivity
import com.richmedia.ideapad.data.DataStore
import com.richmedia.ideapad.data.Note
import com.richmedia.ideapad.databinding.NoteRowItemBinding

class NotesAdapter(private val context: Context) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes: List<Note> = ArrayList()
    private var isRefreshing = false

    init {
        setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        refresh()
    }

    override fun getItemId(position: Int): Long {
        return notes[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = NoteRowItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.textTV.text = note.text
        holder.binding.titleTV.text = note.title

        holder.binding.noteCV.setOnClickListener{
            val intent = Intent(context, NewNoteActivity::class.java)
            intent.putExtra("note", note)
            context.startActivity(intent)

        }

    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        DataStore.execute {
            val notes = DataStore.notes.getAll()
            Handler(Looper.getMainLooper()).post {
                this@NotesAdapter.notes = notes
                notifyDataSetChanged()
                isRefreshing = false
            }
        }
    }

    class NotesViewHolder(val binding: NoteRowItemBinding): RecyclerView.ViewHolder(binding.root)

}
