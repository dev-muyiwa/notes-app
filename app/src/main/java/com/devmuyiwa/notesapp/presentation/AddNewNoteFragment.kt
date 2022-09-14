package com.devmuyiwa.notesapp.presentation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.devmuyiwa.notesapp.R
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.databinding.FragmentAddNewNoteBinding

class AddNewNoteFragment : Fragment() {
    private var _binding: FragmentAddNewNoteBinding? = null
    private val binding get() = _binding!!
    private val allNotesViewModel: AllNotesViewModel by viewModels()
    private val sharedNotesViewModel: SharedNotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noteCategory.onItemSelectedListener = sharedNotesViewModel.listener
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_new_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_new_note -> {
                        addNoteToDb()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun addNoteToDb() {
        val title = binding.noteTitle.text.toString()
        val category = binding.noteCategory.selectedItem.toString()
        val description = binding.noteDescription.text.toString()
        val validation = sharedNotesViewModel.validateUserInput(title, description)
        if (validation) {
            val newNote = Note(0, title, sharedNotesViewModel.stringToCategory(category), description)
            allNotesViewModel.insertNote(newNote)
            Toast.makeText(requireContext(), "Note added successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addNewNoteFragment_to_allNotesFragment)
        } else {
            Toast.makeText(requireContext(), "Text fields cannot be empty", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}