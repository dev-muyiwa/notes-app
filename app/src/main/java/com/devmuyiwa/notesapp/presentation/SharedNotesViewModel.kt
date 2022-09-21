package com.devmuyiwa.notesapp.presentation

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devmuyiwa.notesapp.data.model.DbInstance
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.data.repository.NotesRepository
import com.devmuyiwa.notesapp.domain.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedNotesViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = DbInstance.getDatabase(app).getNotesDao()
    private val repository: NotesRepository = NotesRepository(dao)

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun validateUserInput(title: String, description: String, category: String?): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) ||
            TextUtils.isEmpty(category)
        ) false
        else !(title.isEmpty() || description.isEmpty() || category.isNullOrEmpty())
    }

    fun categoryToInt(category: Category): Int {
        return when (category) {
            Category.UNCATEGORIZED -> 0
            Category.HOME -> 1
            Category.WORK -> 2
            Category.STUDY -> 3
            Category.PERSONAL -> 4
        }
    }

    fun stringToCategory(category: String): Category {
        return when (category) {
            "Uncategorized" -> Category.UNCATEGORIZED
            "Home" -> Category.HOME
            "Work" -> Category.WORK
            "Study" -> Category.STUDY
            "Personal" -> Category.PERSONAL
            else -> Category.UNCATEGORIZED
        }
    }

}