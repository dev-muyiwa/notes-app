package com.devmuyiwa.notesapp.data.model

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

object DbInstance {
    @Volatile
    private var instance: NotesDb? = null

    @OptIn(InternalCoroutinesApi::class)
    fun getDatabase(context: Context): NotesDb {
        val temp = instance
        if (temp != null) {
            return temp
        }
        synchronized(this) {
            val dbInstance = Room.databaseBuilder(
                context.applicationContext, NotesDb::class.java, "notes.db").build()
            instance = dbInstance
            return dbInstance
        }
    }
}