package com.example.random.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.adapter.local.VokabelDataBase
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.adapter.local.getDatabase
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.FragmentTranslationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TranslationFragment : Fragment() {

    private lateinit var binding: FragmentTranslationBinding
    private lateinit var dataBase: VokabelDataBaseDao
    private lateinit var vocableAdapter: VocableAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LayoutManager und SnapHelper Setup
        setupRecyclerView()

        // Datenbankzugriff und Initialisierung
        initializeDatabase()

        // Adapter mit einer leeren Liste initialisieren
        vocableAdapter = VocableAdapter(mutableListOf())
        binding.vocabularyRecyclerView.adapter = vocableAdapter

        // Daten laden und in den Adapter einfügen
        loadVocabulariesIntoAdapter()

        // ItemTouchHelper Setup
        setupItemTouchHelper()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.vocabularyRecyclerView.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.vocabularyRecyclerView)
    }

    private fun initializeDatabase() {
        context?.let {
            dataBase = getDatabase(it).vokabelDataBaseDao()
        }
    }

    private fun loadVocabulariesIntoAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            // Annahme, dass die Methode `getAllVocabItems()` existiert und eine Liste von VocabItem zurückgibt
            val vocabularyList = dataBase.getAllVocabItems()
            CoroutineScope(Dispatchers.Main).launch {
                vocableAdapter.updateList(vocabularyList)
            }
        }
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Handle swipe event
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.vocabularyRecyclerView)
    }
}



