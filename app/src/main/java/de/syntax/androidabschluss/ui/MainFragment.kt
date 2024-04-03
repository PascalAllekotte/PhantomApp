package de.syntax.androidabschluss.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.adapter.NoteAdapter
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.databinding.FragmentMainBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import de.syntax.androidabschluss.viewmodel.VokabelViewModel


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var vocableAdapter: VocableAdapter
    private lateinit var vokabelViewModel: VokabelViewModel

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

        binding.toolbarLayout.backbutton.visibility = View.GONE
        binding.toolbarLayout.titletexttool.setText("Main")

        vokabelViewModel = ViewModelProvider(this).get(VokabelViewModel::class.java)
        setupRecyclerView()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setupRecyclerViewNote()

        vokabelViewModel.vokabelList.observe(viewLifecycleOwner) { vocabularyList ->
            val favorite = vocabularyList.filter { it.favorite }
            vocableAdapter.updateList(favorite)
        }

        // Zeigt die letzten Einträge als erstres
        noteViewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            noteAdapter.updateList(noteList.reversed())
        }
    }

    private fun setupRecyclerView() {
        vocableAdapter = VocableAdapter(mutableListOf()) { vocabItem ->
            // Handle vocab item click
        }
        binding.vocabularyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.vocabularyRecyclerView.adapter = vocableAdapter
        LinearSnapHelper().attachToRecyclerView(binding.vocabularyRecyclerView)
    }

    private fun setupRecyclerViewNote() {
        noteAdapter = NoteAdapter(mutableListOf()) { noteItem ->
            noteViewModel.delete(noteItem)
        }

        binding.noterecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.noterecyclerView.adapter = noteAdapter
        binding.noterecyclerView.setHasFixedSize(false)
        LinearSnapHelper().attachToRecyclerView(binding.noterecyclerView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}