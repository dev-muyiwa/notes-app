package com.devmuyiwa.notesapp.presentation.editNote

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
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
import com.devmuyiwa.notesapp.presentation.SharedNotesViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "EditNoteFragment"

class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EditNoteFragmentArgs>()
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
        inflateToolbar()
    }

    override fun onResume() {
        super.onResume()
        fetchDataIntoViews()
    }

    private fun fetchDataIntoViews() {
        binding.noteTitle.setText(args.currentNote.title)
        binding.noteDescription.setText(args.currentNote.description)
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.layout_list_text, categories)
        val dropDown = binding.dropdownEditable
        val categoryIndex = sharedNotesViewModel.categoryToInt(args.currentNote.category)
        dropDown.setAdapter(arrayAdapter)
        dropDown.setText(dropDown.adapter.getItem(categoryIndex).toString())
        arrayAdapter.filter.filter(null)
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
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateNoteToDb() {
        val title = binding.noteTitle.text.toString()
        val category = binding.categoryDropdown.editText?.text.toString()
        val description = binding.noteDescription.text.toString()
        val validation = sharedNotesViewModel.validateUserInput(title, description, category)
        if (validation) {
            val calendar = Calendar.getInstance()
            val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(calendar.time)
            val updatedNote = Note(args.currentNote.id, date, title,
                sharedNotesViewModel.stringToCategory(category), description)
            sharedNotesViewModel.updateNote(updatedNote)
            Log.d(TAG, "Note updated to  Room successfully")
            Toast.makeText(requireContext(), "Note updated successfully", Toast.LENGTH_SHORT)
                .show()
            val action =
                EditNoteFragmentDirections.actionEditNoteFragmentToDetailedNoteFragment(updatedNote)
            findNavController().navigate(action)
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "Field(s) cannot be empty!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}