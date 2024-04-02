package com.example.random.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.VocableAdapter
import de.syntax.androidabschluss.databinding.FragmentTranslationBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel
import de.syntax.androidabschluss.viewmodel.VokabelViewModel

class TranslationFragment : Fragment() {

    private lateinit var binding: FragmentTranslationBinding
    private lateinit var vocableAdapter: VocableAdapter
    private lateinit var viewModel: VokabelViewModel
    private lateinit var noteViewModel: NoteViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { colorInt ->
            val colorStateList = ColorStateList.valueOf(colorInt)
            binding.cardviewtr.setStrokeColor(colorStateList)
            binding.cardviewtr2.setStrokeColor(colorStateList)

        }


        viewModel = ViewModelProvider(this).get(VokabelViewModel::class.java)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding.toolbarLayout2.titletexttool.setText("Brainery")
        binding.toolbarLayout2.backbutton.visibility = View.GONE

        binding.btTips.setOnClickListener {
            findNavController().navigate(R.id.deepLFragment)


        }

        //Navigation----------------------------
        binding.btLearning.setOnClickListener {
            findNavController().navigate(R.id.learningFragment)
        }
        binding.btAdd.setOnClickListener {
            findNavController().navigate(R.id.addVokabelFragment)
        }

        binding.btNotes.setOnClickListener {
            findNavController().navigate(R.id.action_translationFragment_to_noteDetailFragment)

        }

    }

}


