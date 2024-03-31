package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.adapter.local.NoteDataBase
import de.syntax.androidabschluss.adapter.local.getDatabaseNote
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.databinding.FragmentNoteDetailBinding
import de.syntax.androidabschluss.viewmodel.NoteViewModel
import kotlinx.coroutines.launch


class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    lateinit var database: NoteDataBase
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnAdd.setOnClickListener{
            addNoteItem()

            binding.etTitle.text = null
            binding.etContent.text = null

        }
        binding.toolbarLayout2.titletext.setText("Add Notes")
        binding.toolbarLayout2.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }


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




}