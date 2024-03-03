package com.example.notesapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding

    private val myRef = Firebase.database.reference

    private val notesList = ArrayList<Note>()

    lateinit var notesAdapter: NotesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)


        setSupportActionBar(mainBinding.ToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mainBinding.floatingActionButton.setOnClickListener {

            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)

        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val noteTodeleteId = notesAdapter.getNoteId(viewHolder.adapterPosition)
                myRef.child(noteTodeleteId).removeValue()

                Toast.makeText(applicationContext, "Delete successfull ", Toast.LENGTH_LONG).show()

            }

        }).attachToRecyclerView(mainBinding.recyclerView)

        retrieveDataFromDatabase()

    }

    private fun retrieveDataFromDatabase(){

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                notesList.clear()

                for (eachNote in snapshot.children ) {

                    val note = eachNote.getValue(Note::class.java)

                    if( note != null ){

                        println("NoteId : ${note.noteId}")
                        println("UserName : ${note.userName}")
                        println("Date : ${note.noteDate}")
                        println("Note : ${note.userNote}")
                        notesList.add(note)

                    }
                    notesAdapter = NotesAdapter(this@MainActivity,notesList)

                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                    mainBinding.recyclerView.adapter = notesAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_delete_all,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.deleteAll){

            showDialogMessage()

        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialogMessage(){

        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete all Notes")
        dialogMessage.setMessage("the action will delete all notes "+"if you you want to delete one note please swip the respective note right or left")
        dialogMessage.setNegativeButton("cancel", DialogInterface.OnClickListener{dialogInterface, i ->

            dialogInterface.cancel()

        })
        dialogMessage.setPositiveButton("yes", DialogInterface.OnClickListener{dialogInterface, i ->

            myRef.removeValue().addOnCompleteListener{ task ->

                if(task.isSuccessful){

                    notesAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "All Notes are deleted", Toast.LENGTH_SHORT).show()

                }else{

                    Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT).show()

                }

            }

        })

        dialogMessage.create().show()

    }

}