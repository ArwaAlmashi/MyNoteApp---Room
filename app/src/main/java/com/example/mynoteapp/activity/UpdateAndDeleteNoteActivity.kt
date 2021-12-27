package com.example.mynoteapp.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynoteapp.R
import com.example.mynoteapp.database.DatabaseHelper
import com.example.mynoteapp.databinding.ActivityUpdateAndDeleteNoteBinding
import com.example.mynoteapp.lightStatueBar
import com.example.mynoteapp.model.Note
import com.example.mynoteapp.model.NoteModel
import com.example.mynoteapp.model.Task
import com.example.mynoteapp.recyclerview.AddTaskAdapter
import com.example.mynoteapp.recyclerview.UpdateTaskAdapter
import com.example.mynoteapp.setFullScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpdateAndDeleteNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateAndDeleteNoteBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var updateTaskAdapter: UpdateTaskAdapter
    private lateinit var note: NoteModel

    private var tasks = arrayListOf<Task>()

    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAndDeleteNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set UI
        setUI()

        // Get note object
        note = intent.getSerializableExtra("note") as NoteModel
        binding.updateTitleNote.setText(note.title)

        // Set Recyclerview
        setRecyclerview()

        // Add Task edittext
        binding.updateTaskEtButton.setOnClickListener {
            tasks= tasksEditText()
            tasks.add(Task("", false))
            updateTaskAdapter.update(tasks)
        }

        // Delete Note
        binding.deleteNoteButton.setOnClickListener {
            databaseHelper.deleteNote(note)
            successAlert("Success Delete")
        }

        // Update Note
        binding.updateNoteButton.setOnClickListener {

            // (1) Take task list from edittext in recyclerview
            var newTasks = arrayListOf<Task>()
            for (i in 0 until updateTaskAdapter.getItemCount()) {
                val viewHolder: UpdateTaskAdapter.UpdateTaskHolder =
                    recyclerView.findViewHolderForAdapterPosition(i) as UpdateTaskAdapter.UpdateTaskHolder
                val task = viewHolder.binding.taskText.text.toString()

                if (task != "") {
                    if (viewHolder.binding.doneTaskCheck.drawable == null ){
                        newTasks.add(Task(task, false))
                    } else {
                        newTasks.add(Task(task, true))
                    }
                }
            }

            // (2) Update note
            val title = binding.updateTitleNote.text.toString()
            note.title = title
            //note.tasks = newTasks

            // (4) Update in SQLite
            updateNote(note)
        }


    }

    private fun setUI() {
        setFullScreen(window)
        lightStatueBar(window)
        Glide.with(this).load(R.drawable.girl2).into(binding.girlImage)
    }

    private fun setRecyclerview() {
        recyclerView = binding.updateTaskRecyclerview
//        updateTaskAdapter = UpdateTaskAdapter(note.tasks)
//        tasks = note.tasks
        recyclerView.adapter = updateTaskAdapter
    }

    private fun updateNote(noteUpdated: NoteModel) {
//        val taskString = convertListObjToString(note.tasks)
//        databaseHelper.updateNote(noteUpdated, taskString!!)
        successAlert("Success Update")
    }

    private fun <T> convertListObjToString(listObj: ArrayList<T>?): String? {
        return Gson().toJson(listObj, object : TypeToken<ArrayList<T>?>() {}.getType())
    }

    private fun tasksEditText() : ArrayList<Task>{
        val taskList = arrayListOf<Task>()
        for (i in 0 until updateTaskAdapter.getItemCount()) {
            val viewHolder: UpdateTaskAdapter.UpdateTaskHolder =
                recyclerView.findViewHolderForAdapterPosition(i) as UpdateTaskAdapter.UpdateTaskHolder
            val task = viewHolder.binding.taskText.text.toString()
            if (task != "") {
                taskList.add(Task(task, false))
            }
        }
        return taskList
    }

    private fun successAlert(message: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.success_add)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val successTextView= dialog.findViewById<TextView>(R.id.success_text)
        val successImageView = dialog.findViewById<ImageView>(R.id.success_image)
        val closeButton = dialog.findViewById<ImageView>(R.id.close_icon)

        successTextView.text = message
        Glide.with(this).load(R.drawable.resource_new).into(successImageView)
        dialog.show()


        closeButton.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

    }
}