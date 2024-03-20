package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.syntax.androidabschluss.databinding.FragmentAssistantsBinding


class AssistantsFragment : Fragment() {
    private lateinit var binding : FragmentAssistantsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding = FragmentAssistantsBinding.inflate(inflater,container,false)
        return binding.root



    }



}
