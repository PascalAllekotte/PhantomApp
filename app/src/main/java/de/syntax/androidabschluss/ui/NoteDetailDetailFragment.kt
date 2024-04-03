package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.syntax.androidabschluss.databinding.FragmentNoteDetailDetailBinding

class NoteDetailDetailFragment : Fragment() {

    private lateinit var binding : FragmentNoteDetailDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


}