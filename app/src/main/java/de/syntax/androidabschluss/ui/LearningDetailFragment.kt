package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.databinding.FragmentLearningDetailBinding
import de.syntax.androidabschluss.viewmodel.VokabelViewModel

class LearningDetailFragment : Fragment() {

    private lateinit var binding: FragmentLearningDetailBinding
    private lateinit var vocableAdapter: VocableAdapter
    private val viewModel: VokabelViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLearningDetailBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.vokabeltest.setOnClickListener {
            findNavController().navigate(R.id.vocabularyTestFragment)
        }

        binding.toolbarLayout.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }
        val blockName1 = arguments?.getString("blockName")

        binding.toolbarLayout.titletext.setText("$blockName1")



        val blockName = arguments?.getString("blockName") ?: return
        viewModel.getVocabItemsByBlock(blockName).observe(viewLifecycleOwner) { vocabItems ->
            vocableAdapter.updateList(vocabItems)
        }

    }


    private fun setupRecyclerView() {
        vocableAdapter = VocableAdapter(mutableListOf()) { vocabItem ->
            viewModel.updateVocabItem(vocabItem)
        }
        binding.blockRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.blockRecyclerView.adapter = vocableAdapter
        LinearSnapHelper().attachToRecyclerView(binding.blockRecyclerView)
    }
}