package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.NoteAdapter
import de.syntax.androidabschluss.adapter.local.NoteDataBase
import de.syntax.androidabschluss.databinding.FragmentNoteDetailBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    lateinit var database: NoteDataBase

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel

    //Zeit einbau für Note
    private var currentTime: String? = null

    //Strokes
    private val sharedViewModel : SharedViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //strokefarbe
        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { color ->
            noteAdapter.updateStrokeColor(color)
        }

        // zurück navigieren
        binding.addbutton.setOnClickListener{
            findNavController().navigate(R.id.noteDetailDetailFragment)
        }

        //such funktion
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText)
                return true
            }
        })


        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        currentTime = sdf.format(Date())

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setupRecyclerViewNote()
        noteViewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            noteAdapter.updateList(noteList.reversed())
        }


        binding.toolbarLayout2.titletext.setText("Add Notes")
        binding.toolbarLayout2.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }

        // note recyclerview

    }



    private fun setupRecyclerViewNote() {
        noteAdapter = NoteAdapter(mutableListOf()) { noteItem ->
            noteViewModel.delete(noteItem)
        }

        binding.noterecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noterecyclerView.adapter = noteAdapter
        LinearSnapHelper().attachToRecyclerView(binding.noterecyclerView)
    }


    // funktion um die Notes rauszufilter mit text
    // egal ob groß oder klein wenn das Suchfeld nicht leer ist
    private fun filterNotes(eingabetext: String?) {
        val filteredList = if (!eingabetext.isNullOrEmpty()) {
            noteViewModel.noteList.value?.filter {
                it.title.contains(eingabetext, ignoreCase = true) || it.content.contains(eingabetext, ignoreCase = true)
            }
        } else {
            noteViewModel.noteList.value // wenn nichts im suchfeld steht gibt es die normale not liste aus datenbank
        }

        // wenn etwas gefililtert wurde wird der adapter aktualisiert
        filteredList?.let {
            noteAdapter.updateList(it)
        }
    }

// todo item klickbar machen

}