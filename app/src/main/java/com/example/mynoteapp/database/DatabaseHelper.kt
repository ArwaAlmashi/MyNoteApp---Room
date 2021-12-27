package com.example.mynoteapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mynoteapp.model.Note
import com.example.mynoteapp.model.NoteModel
import com.example.mynoteapp.model.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 1) {

    private var sqLiteDatabase: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table notes (pk INTEGER PRIMARY KEY AUTOINCREMENT,Title text, Tasks text, IsCompleted text, Date text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}


    // ADD
    fun saveNotes(note: Note, tasksString: String) {
        val contentValues = ContentValues()
        contentValues.put("Title", note.title)
        contentValues.put("Tasks", tasksString)
        contentValues.put("IsCompleted", "${note.isComplete}")
        contentValues.put("Date", note.date)
        sqLiteDatabase.insert("notes", null, contentValues)
    }

    // READ
    fun readData(): ArrayList<NoteModel> {

        val noteModelList = arrayListOf<NoteModel>()
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes", null)

        if (cursor.count < 1) {
            println("No Data Found")
        } else {
            while (cursor.moveToNext()) {

                // Read data
                val pk = cursor.getInt(0)
                val title = cursor.getString(1)
                val tasksString = cursor.getString(2)
                val isCompletedString = cursor.getString(3)
                val date = cursor.getString(4)

                // Convert task & isCompleted
                val tasksL = convertStringToObjList(tasksString)
                val tasks = retuernArrayList(tasksL)
                val isCompleted = convertStringToBoolean(isCompletedString)

                // Add to list
                noteModelList.add(NoteModel(pk, title, isCompleted, date))

            }
        }
        return noteModelList
    }

    // UPDATE
    fun updateNote(note: NoteModel, tasksString: String) {
        val contentValues = ContentValues()
        contentValues.put("Title", note.title)
        contentValues.put("Tasks", tasksString)
        contentValues.put("IsCompleted", "${note.isComplete}")
        contentValues.put("Date", note.date)
        sqLiteDatabase.update("notes", contentValues, "pk = ${note.id}", null)
    }

    // DELETE
    fun deleteNote(note: NoteModel) {
        sqLiteDatabase.delete("notes", "pk = ${note.id}", null)
    }

    // Convertor Functions
    private fun convertStringToObjList(tasksString: String): List<Task> {
        val gson = GsonBuilder().create()
        val model = gson.fromJson(tasksString, Array<Task>::class.java).toList()
        return model
    }

    private fun convertStringToBoolean(isCompletedString: String): Boolean {
        return isCompletedString == "true"
    }

    private fun retuernArrayList(list: List<Task>): ArrayList<Task> {
        var tasks = arrayListOf<Task>()
        for (item in list) {
            tasks.add(item)
        }
        return tasks
    }


}