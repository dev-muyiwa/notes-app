package com.devmuyiwa.notesapp.data.repository

import androidx.lifecycle.LiveData
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.data.model.NotesDao

class NotesRepository(private val dao: NotesDao) {
    val getAllNotes: LiveData<List<Note>> = dao.getAllNotes()
    suspend fun addNewNote(note: Note) {
        dao.addNote(note)
    }

    suspend fun updateNote(note: Note) {
        dao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    suspend fun deleteAllNotes() {
        dao.deleteAllNotes()
    }

    fun searchDb(query: String): LiveData<List<Note>> {
        return dao.searchDb(query)
    }
}