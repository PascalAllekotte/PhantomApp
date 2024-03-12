package de.syntax.androidabschluss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.adapter.LearningAdapter
import de.syntax.androidabschluss.databinding.FragmentLearningBinding
import de.syntax.androidabschluss.viewmodel.VokabelViewModel


class LearningFragment : Fragment() {

    private lateinit var binding: FragmentLearningBinding
    private lateinit var learningAdapter: LearningAdapter
    private val viewModel: VokabelViewModel by lazy {
        ViewModelProvider(this).get(VokabelViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLearningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapter()
        initializeDatabase()
        setupRecyclerView()
        loadVocabulariesIntoAdapter()

        binding.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initializeAdapter() {
        learningAdapter = LearningAdapter(mutableListOf(), viewLifecycleOwner) { blockName ->
            // Navigation zum LearningDetailFragment, mit Übergabe des Blocknamens
            val action = LearningFragmentDirections.actionLearningFragmentToLearningDetailFragment(blockName)
            findNavController().navigate(action)
        }
        binding.blockRecyclerView.adapter = learningAdapter
    }



    private fun loadVocabulariesIntoAdapter() {
        viewModel.uniqueBlockList.observe(viewLifecycleOwner) { uniqueBlocks ->
            learningAdapter.updateList(uniqueBlocks)
        }
    }


    private fun initializeDatabase() {
        // Diese Methode scheint in deinem aktuellen Code redundant zu sein, da die Datenbankinitialisierung bereits in `initializeAdapter` erfolgt.
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.blockRecyclerView.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.blockRecyclerView)
    }
}
