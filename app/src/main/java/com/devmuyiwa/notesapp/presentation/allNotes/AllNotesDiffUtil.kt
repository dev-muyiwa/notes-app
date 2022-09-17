package com.devmuyiwa.notesapp.presentation.allNotes

import androidx.recyclerview.widget.DiffUtil
import com.devmuyiwa.notesapp.data.model.Note

class AllNotesDiffUtil(
    private val listOfOldNotes: List<Note>,
    private val listOfNewNotes: List<Note>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int  = listOfOldNotes.size

    override fun getNewListSize(): Int = listOfNewNotes.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listOfOldNotes[oldItemPosition] === listOfNewNotes[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = listOfOldNotes[oldItemPosition]
        val newNote = listOfNewNotes[newItemPosition]
        return oldNote.id == newNote.id && oldNote.title == newNote.title &&
                oldNote.category == newNote.category && oldNote.description == newNote.description
    }
}