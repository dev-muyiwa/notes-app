package com.devmuyiwa.notesapp.presentation.listOfNotes

import android.content.Context
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devmuyiwa.notesapp.R
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.databinding.LayoutAllNotesBinding
import com.devmuyiwa.notesapp.domain.Category

private const val TAG = "AllNotesAdapter"

class AllNotesAdapter(val context: Context) :
    RecyclerView.Adapter<AllNotesAdapter.NoteViewHolder>() {
    private var listOfNotes = ArrayList<Note>()
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = LayoutAllNotesBinding.bind(itemView)
        fun setColor(note: Note, view: View) {
            when (note.category) {
                Category.UNCATEGORIZED -> view
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                Category.HOME -> view
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                Category.WORK -> view
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                Category.STUDY -> view
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                Category.PERSONAL -> view
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        Log.i(TAG, "onCreateViewHolder successful")
        val view = LayoutInflater.from(context).inflate(R.layout.layout_all_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder : position $position")
        val note = listOfNotes[position]
        holder.binding.noteTitle.text = note.title
        holder.binding.noteContent.text = note.description
        holder.binding.noteBackground.setOnClickListener {
            val action = AllNotesFragmentDirections.actionAllNotesFragmentToEditNoteFragment(note)
            holder.itemView.findNavController().navigate(action)
        }
//        holder.setColor(note, holder.binding.noteHeader)

    }

    override fun getItemCount(): Int = listOfNotes.size

    fun setData(notes: ArrayList<Note>) {
        val diffUtil = AllNotesDiffUtil(listOfNotes, notes)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        this.listOfNotes = notes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}