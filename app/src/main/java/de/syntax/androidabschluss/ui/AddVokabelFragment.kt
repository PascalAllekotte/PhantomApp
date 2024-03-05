package de.syntax.androidabschluss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentAddVokabelBinding


class AddVokabelFragment : Fragment() {

private lateinit var binding: FragmentAddVokabelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddVokabelBinding.inflate(inflater, container, false)
        return binding.root
    }


}