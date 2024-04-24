package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.adapter.LearningAdapter
import de.syntax.androidabschluss.databinding.FragmentLearningBinding
import de.syntax.androidabschluss.viewmodel.SharedViewModel
import de.syntax.androidabschluss.viewmodel.VokabelViewModel


class LearningFragment : Fragment() {

    private lateinit var binding: FragmentLearningBinding
    private lateinit var learningAdapter: LearningAdapter
    private val viewModel: VokabelViewModel by lazy {
        ViewModelProvider(this).get(VokabelViewModel::class.java)
    }

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLearningBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Beobachtet die Farbeinstellung und aktualisiert den Adapter entsprechend
        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { color ->
            learningAdapter.updateStrokeColor(color)
        }

        // Initialisiert Adapter und Datenbank und richtet RecyclerView ein
        initializeAdapter()
        setupRecyclerView()
        loadVocabulariesIntoAdapter()

        // Setzt den Titel der Toolbar und fügt den Zurück-Button hinzu
        binding.toolbarLayout2.titletext.setText("Blocks")
        binding.toolbarLayout2.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    // Initialisiert den Adapter mit einem leeren Listen- und Klickverhalten
    private fun initializeAdapter() {
        learningAdapter = LearningAdapter(mutableListOf(), viewLifecycleOwner) { blockName ->
            val action = LearningFragmentDirections.actionLearningFragmentToLearningDetailFragment(blockName)
            findNavController().navigate(action)
        }
        binding.blockRecyclerView.adapter = learningAdapter
    }

    // Lädt eindeutige Datenblöcke in den Adapter
    private fun loadVocabulariesIntoAdapter() {
        viewModel.uniqueBlockList.observe(viewLifecycleOwner) { uniqueBlocks ->
            learningAdapter.updateList(uniqueBlocks)
        }
    }

    // Richtet das RecyclerView mit einem LinearLayoutManager und einem SnapHelper ein
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.blockRecyclerView.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.blockRecyclerView)
    }
}