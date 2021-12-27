package com.example.mynoteapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mynoteapp.model.NoteModel

@Dao
interface NoteDao {

    @Query( "SELECT * FROM NoteModel")
    fun getAllNotes(): List<NoteModel>

    @Insert
    fun insertNote(note: NoteModel)
}