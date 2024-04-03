package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.databinding.FragmentTermsBinding


class TermsFragment : Fragment() {

    private lateinit var binding : FragmentTermsBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentTermsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbarLayout.titletext.setText("Terms")
        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }

    }


}