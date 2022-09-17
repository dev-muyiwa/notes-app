package com.devmuyiwa.notesapp.presentation.detailedNote

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
import com.devmuyiwa.notesapp.databinding.FragmentDetailedNoteBinding
import com.devmuyiwa.notesapp.presentation.SharedNotesViewModel

class DetailedNoteFragment : Fragment() {
    private var _binding: FragmentDetailedNoteBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailedNoteFragmentArgs>()
    private val sharedNotesViewModel: SharedNotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailedNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateToolbar()
        binding.noteTitle.text = args.currentNoteDetails.title
        binding.date.text = args.currentNoteDetails.date
        binding.noteDescription.text = args.currentNoteDetails.description
    }

    private fun inflateToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_details_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.edit_note -> {
                        val action = DetailedNoteFragmentDirections
                            .actionDetailedNoteFragmentToEditNoteFragment(args.currentNoteDetails)
                        findNavController().navigate(action)
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

    private fun deleteNoteFromDb() {
        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_NotesApp_AlertDialogTheme)
        builder.setPositiveButton("Yes") { _, _ ->
            sharedNotesViewModel.deleteNote(args.currentNoteDetails)
            Toast.makeText(requireContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_detailedNoteFragment_to_AllNotesFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete \"${args.currentNoteDetails.title}?\"")
        builder.setMessage("Are you sure you want to delete this note permanently?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}