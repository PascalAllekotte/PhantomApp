package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.adapter.NoteAdapter
import de.syntax.androidabschluss.adapter.local.NoteDataBase
import de.syntax.androidabschluss.adapter.local.getDatabaseNote
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.databinding.FragmentNoteDetailBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import kotlinx.coroutines.launch


class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    lateinit var database: NoteDataBase

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setupRecyclerViewNote()
        noteViewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            noteAdapter.updateList(noteList.reversed())
        }

        binding.btnAdd.setOnClickListener{
            addNoteItem()

            binding.etTitle.text = null
            binding.etContent.text = null

        }
        binding.toolbarLayout2.titletext.setText("Add Notes")
        binding.toolbarLayout2.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }

        // note recyclerview

    }

    private fun addNoteItem() {
        val ntitle = binding.etTitle.text.toString()
        val ncontent = binding.etContent.text.toString()

        if (ntitle.isNotEmpty() && ncontent.isNotEmpty()) {
            val newNoteItem = NoteItem(
                title = ntitle,
                content = ncontent
            )

            lifecycleScope.launch {

                val dp = getDatabaseNote(requireContext())
                dp.noteDataBaseDao().insert(newNoteItem)

            }


        }
    }

    private fun setupRecyclerViewNote() {
        noteAdapter = NoteAdapter(mutableListOf()) { noteItem ->
            noteViewModel.delete(noteItem)
        }

        binding.noterecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.noterecyclerView.adapter = noteAdapter
        LinearSnapHelper().attachToRecyclerView(binding.noterecyclerView)
    }




}