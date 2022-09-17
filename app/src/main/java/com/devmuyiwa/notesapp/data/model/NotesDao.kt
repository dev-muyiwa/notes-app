package com.devmuyiwa.notesapp.data.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {
    @Query("SELECT * FROM note ORDER BY date DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note WHERE title LIKE :query ORDER BY date DESC")
    fun searchDb(query: String): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE category LIKE :category")
    fun sortByCategory(category: String): LiveData<List<Note>>
}