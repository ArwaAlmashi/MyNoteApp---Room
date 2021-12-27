package com.example.mynoteapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.mynoteapp.database.DatabaseHelper
import com.example.mynoteapp.database.NoteDatabase
import com.example.mynoteapp.databinding.ActivityNoteBinding
import com.example.mynoteapp.lightStatueBar
import com.example.mynoteapp.model.Note
import com.example.mynoteapp.model.NoteModel
import com.example.mynoteapp.model.Task
import com.example.mynoteapp.recyclerview.NotesAdapter
import com.example.mynoteapp.recyclerview.RecentNotesAdapter
import com.example.mynoteapp.setFullScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager




class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    private lateinit var notesRV: RecyclerView
    private lateinit var notesAdapter: NotesAdapter

    //private lateinit var notes: ArrayList<NoteModel>
    private var notes = arrayListOf<NoteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set UI
        setFullScreen(window)
        lightStatueBar(window)


        // ROOM
        NoteDatabase.getInstance(applicationContext)

        CoroutineScope(IO).launch {
            val notes = NoteDatabase.getInstance(applicationContext).NoteDao().getAllNotes() as ArrayList<NoteModel>
            println("11111111111111111111111111111111111")
            println(notes)
            // setRecyclerview()
            runOnUiThread {
                notesRV = binding.allNotesRecyclerview
                notesAdapter = NotesAdapter(notes, this@NoteActivity)
                notesRV.adapter = notesAdapter
                notesAdapter.notifyDataSetChanged()
            }

        }





        // Go to Add Note
        binding.addIcon.setOnClickListener {
            val intent = Intent(this, AddNewNoteActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setRecyclerview() {
        if (notes.size != 0) {
            notesRV = binding.allNotesRecyclerview
            notesAdapter = NotesAdapter(notes, this)
            notesRV.adapter = notesAdapter
            notesAdapter.notifyDataSetChanged()
        }
    }

}