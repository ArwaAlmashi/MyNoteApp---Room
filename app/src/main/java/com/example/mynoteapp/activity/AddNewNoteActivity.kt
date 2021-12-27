package com.example.mynoteapp.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynoteapp.R
import com.example.mynoteapp.database.DatabaseHelper
import com.example.mynoteapp.database.NoteDatabase
import com.example.mynoteapp.databinding.ActivityAddNewNoteBinding
import com.example.mynoteapp.lightStatueBar
import com.example.mynoteapp.model.Note
import com.example.mynoteapp.model.NoteModel
import com.example.mynoteapp.model.Task
import com.example.mynoteapp.recyclerview.AddTaskAdapter
import com.example.mynoteapp.setFullScreen
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddNewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewNoteBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var addTaskAdapter: AddTaskAdapter
    private lateinit var editTextTaskArray: ArrayList<String>

    private var tasks = arrayListOf<Task>()

    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set UI
        setUI()

        // Tasks Recyclerview
        setRecyclerview()

        // Add Task edittext
        binding.addTaskEtButton.setOnClickListener {
            editTextTaskArray.add("")
            addTaskAdapter.update(editTextTaskArray)
        }

        // Add Note
        binding.addNoteButton.setOnClickListener {

            // (1) Take task list from edittext in recyclerview
            for (i in 0 until addTaskAdapter.getItemCount()) {
                val viewHolder: AddTaskAdapter.AddTaskHolder =
                    recyclerView.findViewHolderForAdapterPosition(i) as AddTaskAdapter.AddTaskHolder
                val task = viewHolder.binding.taskText.text.toString()
                if (task != "") {
                    tasks.add(Task(task, false))
                }
            }

            // (2) Current date
            val date = SimpleDateFormat("dd/M/yyyy")
            val currentDate = date.format(Date())

            // (3) Create Note Object
            val title = binding.newTitleNote.text.toString()
            val note = NoteModel(0,title,false, currentDate)

            // (4) Store in SQLite
            store(note)
        }

    }

    private fun setUI() {
        setFullScreen(window)
        lightStatueBar(window)
        Glide.with(this).load(R.drawable.girl2).into(binding.imagee)
    }

    private fun setRecyclerview() {
        editTextTaskArray = arrayListOf("")
        recyclerView = binding.addTaskRecyclerview
        addTaskAdapter = AddTaskAdapter(editTextTaskArray)
        recyclerView.adapter = addTaskAdapter
    }

    private fun store(note: NoteModel) {
        NoteDatabase.getInstance(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            NoteDatabase.getInstance(applicationContext).NoteDao().insertNote(note)
        }
        successAlert(this)
    }

    private fun <T> convertListObjToString(listObj: ArrayList<T>?): String? {
        return Gson().toJson(listObj, object : TypeToken<ArrayList<T>?>() {}.getType())
    }

    private fun successAlert(context: Context) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.success_add)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val successImageView = dialog.findViewById<ImageView>(R.id.success_image)
        val closeButton = dialog.findViewById<ImageView>(R.id.close_icon)

        Glide.with(this).load(R.drawable.resource_new).into(successImageView)
        dialog.show()


        closeButton.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

    }


}