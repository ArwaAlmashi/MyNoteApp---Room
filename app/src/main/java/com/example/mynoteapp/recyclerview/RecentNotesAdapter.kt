package com.example.mynoteapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynoteapp.databinding.RecentlyNotesCardBinding
import com.example.mynoteapp.model.Note

class RecentNotesAdapter(var noteList: ArrayList<Note>) :
    RecyclerView.Adapter<RecentNotesAdapter.RecentNoteHolder>() {
    class RecentNoteHolder ( val binding: RecentlyNotesCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentNotesAdapter.RecentNoteHolder {
        return RecentNoteHolder(
            RecentlyNotesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecentNotesAdapter.RecentNoteHolder, position: Int) {
        val note = noteList[position]
        holder.binding.apply {
        }
    }

    override fun getItemCount() = noteList.size

    fun update(newNoteList: ArrayList<Note>){
        noteList = newNoteList
    }
}