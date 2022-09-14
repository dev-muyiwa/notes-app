package com.devmuyiwa.notesapp.presentation

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devmuyiwa.notesapp.R
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.domain.Category

class SharedNotesViewModel(app: Application) : AndroidViewModel(app) {
    val emptyDb: MutableLiveData<Boolean> = MutableLiveData(false)

    val listener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as TextView).setCompoundDrawables(
                    ContextCompat.getDrawable(app.applicationContext, R.drawable.ic_home),
                    null, null, null)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    fun isDbEmpty(notes: List<Note>){
        emptyDb.value = notes.isEmpty()
    }

    fun validateUserInput(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) false
        else !(title.isEmpty() || description.isEmpty())
    }

    fun categoryToInt(category: Category): Int{
        return when(category){
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