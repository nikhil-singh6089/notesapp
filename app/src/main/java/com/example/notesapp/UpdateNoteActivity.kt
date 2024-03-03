package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityUpdateNoteBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var updateNoteBinding : ActivityUpdateNoteBinding

    private val myRef = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateNoteBinding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        val view = updateNoteBinding.root
        setContentView(view)

        getAndSetData()

        updateNoteBinding.UpdateNotebutton.setOnClickListener{

            updateData()

        }

    }

    private fun getAndSetData(){

        val userName = intent.getStringExtra("userName")
        val noteDate = intent.getStringExtra("noteDate")
        val userNote = intent.getStringExtra("userNote")

        updateNoteBinding.editTextUpdateName.setText(userName)
        updateNoteBinding.editTextUpdateDate.setText(noteDate)
        updateNoteBinding.editTextUpdateNote.setText(userNote)

    }

    private fun updateData(){

        val updatedName = updateNoteBinding.editTextUpdateName.text.toString()
        val updatedDate = updateNoteBinding.editTextUpdateDate.text.toString()
        val updatedNote = updateNoteBinding.editTextUpdateNote.text.toString()
        val noteId = intent.getStringExtra("noteId").toString()

        val noteMap = mutableMapOf<String,Any>()
        noteMap["noteId"]= noteId
        noteMap["userName"]=updatedName
        noteMap["noteData"]=updatedDate
        noteMap["userNote"]=updatedNote

        myRef.child(noteId).updateChildren(noteMap).addOnCompleteListener { task ->

            if(task.isSuccessful){

                Toast.makeText(applicationContext, "user data is updated successfully",Toast.LENGTH_SHORT).show()
                finish()

            }else{

                Toast.makeText(applicationContext, task.exception.toString() ,Toast.LENGTH_LONG).show()

            }

        }

    }

}