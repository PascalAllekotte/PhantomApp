package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.NoteAdapter
import de.syntax.androidabschluss.adapter.local.NoteDataBase
import de.syntax.androidabschluss.databinding.FragmentNoteDetailBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addbutton.setOnClickListener{
            findNavController().navigate(R.id.noteDetailDetailFragment)
        }

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
/**
    private fun addNoteItem() {

        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        currentTime = sdf.format(Date())


        if (ntitle.isNotEmpty() && ncontent.isNotEmpty()) {
            val newNoteItem = NoteItem(
                title = ntitle,
                content = ncontent,
                dateTime = currentTime
            )

            lifecycleScope.launch {
                val dp = getDatabaseNote(requireContext())
                dp.noteDataBaseDao().insert(newNoteItem)
            }
        }
    }
**/
    private fun setupRecyclerViewNote() {
        noteAdapter = NoteAdapter(mutableListOf()) { noteItem ->
            noteViewModel.delete(noteItem)
        }

        binding.noterecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noterecyclerView.adapter = noteAdapter
        LinearSnapHelper().attachToRecyclerView(binding.noterecyclerView)
    }




}