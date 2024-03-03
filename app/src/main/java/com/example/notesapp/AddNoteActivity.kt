package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddNoteActivity : AppCompatActivity() {

    private lateinit var addNoteBinding : ActivityAddNoteBinding

    private val myRef = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNoteBinding = ActivityAddNoteBinding.inflate(layoutInflater)
        val view = addNoteBinding.root
        setContentView(view)

        addNoteBinding.AddNotebutton.setOnClickListener {

            addNoteToDatabase()

        }

    }

    private fun addNoteToDatabase(){

        val userName : String = addNoteBinding.editTextAddName.text.toString()
        val noteDate : String = addNoteBinding.editTextAddDate.text.toString()
        val userNote : String = addNoteBinding.editTextAddNote.text.toString()
        val noteId : String = myRef.push().key.toString()

        val note = Note(noteId,userName,noteDate,userNote)

        myRef.child(noteId).setValue(note).addOnCompleteListener { task ->

            if (task.isSuccessful){

                Toast.makeText(applicationContext, "Data Send Successfully", Toast.LENGTH_SHORT).show()
                finish()

            }else{

                Toast.makeText(applicationContext, task.exception.toString() , Toast.LENGTH_SHORT).show()

            }

        }

    }

}