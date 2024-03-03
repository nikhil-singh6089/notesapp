package com.example.notesapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.UsersItemBinding

class NotesAdapter(private var context: MainActivity, private var notesList: ArrayList<Note> ) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val adapterbinding: UsersItemBinding) : RecyclerView.ViewHolder(adapterbinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = UsersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return NoteViewHolder(binding)

    }

    override fun getItemCount(): Int {

        return notesList.size

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.adapterbinding.textViewName.text = notesList[position].userName
        holder.adapterbinding.textViewDate.text = notesList[position].noteDate
        holder.adapterbinding.textViewNote.text = notesList[position].userNote

        holder.adapterbinding.linearLayout.setOnClickListener{

            val intent = Intent(context,UpdateNoteActivity::class.java)
            intent.putExtra("noteId",notesList[position].noteId)
            intent.putExtra("userName",notesList[position].userName)
            intent.putExtra("noteDate",notesList[position].noteDate)
            intent.putExtra("userNote",notesList[position].userNote)
            context.startActivity(intent)

        }

    }

    fun getNoteId(position: Int) : String {

        return notesList[position].noteId

    }


}