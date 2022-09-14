package com.devmuyiwa.notesapp.presentation

import android.app.Application
import androidx.lifecycle.*
import com.devmuyiwa.notesapp.data.model.DbInstance
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllNotesViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = DbInstance.getDatabase(app).getNotesDao()
    private val repository: NotesRepository = NotesRepository(dao)
    val getAllNotes: LiveData<List<Note>> = repository.getAllNotes

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewNote(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteNote(note)
        }
    }
    
    fun deleteAllNotes(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllNotes()
        }
    }

    fun searchDb(query: String): LiveData<List<Note>>{
        return repository.searchDb(query)
    }
}