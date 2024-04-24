package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSelectTextBinding
import soup.neumorphism.NeumorphImageButton


class SelectTextFragment : Fragment() {

    private lateinit var binding : FragmentSelectTextBinding
    private val selectArgs : SelectTextFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTextBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selecttext.text = selectArgs.selectedMessage

        val close = view.findViewById<NeumorphImageButton>(R.id.backbuttonselect)
        close.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}