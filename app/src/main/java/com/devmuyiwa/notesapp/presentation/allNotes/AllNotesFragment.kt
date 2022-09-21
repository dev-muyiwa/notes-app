package com.devmuyiwa.notesapp.presentation.allNotes

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.devmuyiwa.notesapp.R
import com.devmuyiwa.notesapp.data.model.Note
import com.devmuyiwa.notesapp.databinding.FragmentAllNotesBinding
import com.devmuyiwa.notesapp.domain.SpanSize

class AllNotesFragment : Fragment() {
    private var _binding: FragmentAllNotesBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel: AllNotesViewModel by viewModels()
    private var notesAdapter: AllNotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateToolbar()
        notesAdapter = AllNotesAdapter(requireContext())
        noteViewModel.getAllNotes.observe(viewLifecycleOwner) { listOfNotes ->
            noteViewModel.isDbEmpty(listOfNotes)
            notesAdapter?.setData(listOfNotes as ArrayList<Note>)
        }
        binding.listOfNotesRecyclerView.adapter = notesAdapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        layoutManager.reverseLayout = false
        layoutManager.spanSizeLookup = SpanSize(3, 1, 2)
        binding.listOfNotesRecyclerView.layoutManager = layoutManager
        binding.listOfNotesRecyclerView.hasFixedSize()

        noteViewModel.emptyDb.observe(viewLifecycleOwner) {
            displayNoData(it)
        }

        binding.addNewNote.setOnClickListener {
            findNavController().navigate(R.id.action_allNotesFragment_to_addNewNoteFragment)
        }
    }

    private fun inflateToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider, SearchView.OnQueryTextListener {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list_fragment, menu)
                val search = menu.findItem(R.id.search_note)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.queryHint = "Search by note title"
                searchView?.setOnQueryTextListener(this)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_all_notes -> {
                        deleteAllNotes()
                        true
                    }
                    else -> false
                }
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDb(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchDb(newText)
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun deleteAllNotes() {
        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_NotesApp_AlertDialogTheme)
        builder.setPositiveButton("Yes") { _, _ ->
            noteViewModel.deleteAllNotes()
            Toast.makeText(requireContext(), "All Notes deleted successfully", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to delete all your notes permanently?")
        builder.create().show()
    }

    private fun displayNoData(isEmpty: Boolean) {
        if (isEmpty) {
            binding.noNotes.visibility = View.VISIBLE
            binding.listOfNotesRecyclerView.visibility = View.GONE
        } else {
            binding.noNotes.visibility = View.GONE
            binding.listOfNotesRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun searchDb(query: String?) {
        val searchQuery = "%$query%"
        noteViewModel.searchDb(searchQuery).observe(viewLifecycleOwner) { listOfNotes ->
            listOfNotes?.let {
                notesAdapter?.setData(it as ArrayList<Note>)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}