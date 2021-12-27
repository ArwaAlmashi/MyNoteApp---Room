package com.example.mynoteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.mynoteapp.model.NoteModel

@Database(entities = [NoteModel::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        var instance: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {

            return instance?: synchronized(Any()) {
                instance?: buildDatabase(context).also { instance=it}
            }

//            if (instance != null) {
//                return instance as NoteDatabase
//            }
//            instance = Room.databaseBuilder(context, NoteDatabase::class.java, "mydatabase")
//                .run { allowMainThreadQueries() }.build()
//            return instance as NoteDatabase

        }

        private fun buildDatabase(context: Context): NoteDatabase {
            return Room.databaseBuilder(context.applicationContext,
                NoteDatabase::class.java, "mydatabase").build()
        }
    }

    abstract fun NoteDao(): NoteDao
}