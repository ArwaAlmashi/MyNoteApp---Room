package com.example.mynoteapp.recyclerview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.mynoteapp.activity.UpdateAndDeleteNoteActivity
import com.example.mynoteapp.databinding.NotesCardBinding
import com.example.mynoteapp.model.Note
import com.example.mynoteapp.model.NoteModel
import com.example.mynoteapp.model.Task

class NotesAdapter(var noteList: ArrayList<NoteModel>, var context: Context) :
    RecyclerView.Adapter<NotesAdapter.AllNotesHolder>() {
    class AllNotesHolder(val binding: NotesCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllNotesHolder {
        return AllNotesHolder(
            NotesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: AllNotesHolder, position: Int) {
        val note = noteList[position]
        holder.binding.apply {
            titleNoteTv.text = note.title
            //progressValue(note.tasks, tasksProgressbar)
        }
        holder.binding.allNotesCard.setOnClickListener {
            val  intent = Intent(context, UpdateAndDeleteNoteActivity::class.java )
            intent.putExtra("note", note)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = noteList.size

    fun update(newNoteList: ArrayList<NoteModel>) {
        noteList = newNoteList
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun progressValue(tasks: ArrayList<Task>, progressBar: ProgressBar) {
        var progressBarAmount = 0
        if (tasks.size != 0) {
            progressBar.max = tasks.size
            for (task in tasks) {
                if (task.taskIsComplete) {
                    progressBarAmount++
                }
            }
            progressBar.setProgress(progressBarAmount, true)
        }
    }

}