package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSelectTextBinding


class SelectTextFragment : Fragment() {

    private lateinit var binding : FragmentSelectTextBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTextBinding.inflate(inflater,container,false)
        return inflater.inflate(R.layout.fragment_select_text, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.titletext.setText("Select")
        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }



    }

}