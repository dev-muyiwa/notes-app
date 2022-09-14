package com.devmuyiwa.notesapp.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devmuyiwa.notesapp.R
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.databinding.FragmentEditNoteBinding

class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EditNoteFragmentArgs>()
    private val allNotesViewModel: AllNotesViewModel by viewModels()
    private val sharedNotesViewModel: SharedNotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataIntoViews()
        inflateToolbar()
    }

    private fun fetchDataIntoViews() {
        binding.noteTitle.setText(args.currentNote.title)
        binding.noteDescription.setText(args.currentNote.description)
        binding.noteCategory.setSelection(sharedNotesViewModel.categoryToInt(args.currentNote
            .category))
        binding.noteCategory.onItemSelectedListener = sharedNotesViewModel.listener
    }


    private fun inflateToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_update_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.update_note -> {
                        updateNoteToDb()
                        true
                    }
                    R.id.delete_note -> {
                        deleteNoteFromDb()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateNoteToDb() {
        val title = binding.noteTitle.text.toString()
        val category = binding.noteCategory.selectedItem.toString()
        val description = binding.noteDescription.text.toString()
        val validation = sharedNotesViewModel.validateUserInput(title, description)
        if (validation) {
            val updatedNote = Note(args.currentNote.id, title,
                sharedNotesViewModel.stringToCategory(category), description)
            allNotesViewModel.updateNote(updatedNote)
            Toast.makeText(requireContext(), "Note updated successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editNoteFragment_to_allNotesFragment)
        } else {
            Toast.makeText(requireContext(), "Text fields cannot be empty", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun deleteNoteFromDb() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            allNotesViewModel.deleteNote(args.currentNote)
            Toast.makeText(requireContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editNoteFragment_to_allNotesFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentNote.title}?")
        builder.setMessage("Are you sure you want to delete this note permanently?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}