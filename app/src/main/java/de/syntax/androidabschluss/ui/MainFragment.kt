package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.adapter.NoteAdapter
import de.syntax.androidabschluss.databinding.FragmentMainBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        setupRecyclerViewNote()

        noteViewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            noteAdapter.updateList(noteList)
        }
    }

    private fun setupRecyclerViewNote() {
        noteAdapter = NoteAdapter(mutableListOf()) { noteItem ->
            // Löschoperation, wenn auf den Lösch-Button geklickt wird
            noteViewModel.delete(noteItem)
        }

        binding.noterecyclerView.layoutManager = LinearLayoutManager(context)
        binding.noterecyclerView.adapter = noteAdapter
        LinearSnapHelper().attachToRecyclerView(binding.noterecyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
