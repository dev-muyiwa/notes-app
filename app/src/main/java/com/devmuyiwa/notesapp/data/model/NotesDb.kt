package com.devmuyiwa.notesapp.data.model

import androidx.room.*
import com.devmuyiwa.notesapp.data.*

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class NotesDb : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}