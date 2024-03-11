package com.example.random.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.databinding.FragmentTranslationBinding
import de.syntax.androidabschluss.viewmodel.VokabelViewModel

class TranslationFragment : Fragment() {

    private lateinit var binding: FragmentTranslationBinding
    private lateinit var vocableAdapter: VocableAdapter
    private lateinit var viewModel: VokabelViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(VokabelViewModel::class.java)


        //Navigation----------------------------
        binding.btLearning.setOnClickListener {
            findNavController().navigate(R.id.learningFragment)
        }
        binding.btAdd.setOnClickListener {
            findNavController().navigate(R.id.addVokabelFragment)
        }


        //Recyclerview----------------------------
        setupRecyclerView()
        setupItemTouchHelper()

        viewModel.vokabelList.observe(viewLifecycleOwner) { vocabularyList ->
            vocableAdapter.updateList(vocabularyList)
        }
    }

    private fun setupRecyclerView() {
        vocableAdapter = VocableAdapter(mutableListOf()) { vocabItem ->
            viewModel.updateVocabItem(vocabItem)
        }

        binding.vocabularyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.vocabularyRecyclerView.adapter = vocableAdapter
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.vocabularyRecyclerView)
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.vocabularyRecyclerView)
    }
}


