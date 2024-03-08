package de.syntax.androidabschluss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.LearningAdapter
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.adapter.local.getDatabase
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.FragmentLearningBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LearningFragment : Fragment() {

    private lateinit var binding: FragmentLearningBinding
    private lateinit var dataBase: VokabelDataBaseDao
    private lateinit var learningAdapter: LearningAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLearningBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadVocabulariesIntoAdapter()
        initializeDatabase()
        setupRecyclerView()
        learningAdapter = LearningAdapter(mutableListOf(), dataBase)
        binding.blockRecyclerView.adapter = learningAdapter


        binding.backbutton.setOnClickListener{
            findNavController().popBackStack()

        }


    }

    private fun loadVocabulariesIntoAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            val vocabularyList = dataBase.getAllVocabItems()
            CoroutineScope(Dispatchers.Main).launch {
                learningAdapter.updateList(vocabularyList)
            }
        }
    }
    private fun initializeDatabase() {
        context?.let {
            dataBase = getDatabase(it).vokabelDataBaseDao()
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.blockRecyclerView.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.blockRecyclerView)
    }




}