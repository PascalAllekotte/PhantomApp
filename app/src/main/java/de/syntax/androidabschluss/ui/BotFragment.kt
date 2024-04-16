package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentBotBinding


class BotFragment : Fragment() {

    private lateinit var binding : FragmentBotBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBotBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGenerate.setOnClickListener {
            findNavController().navigate(R.id.action_botFragment_to_pictureGeneratorFragment)
        }
        binding.btnAssistant.setOnClickListener {
            findNavController().navigate(R.id.action_botFragment_to_assistantDetailFragment)
        }

        binding.btnBots.setOnClickListener {
            findNavController().navigate(R.id.action_botFragment_to_assistantsFragment)
        }
        binding.pictures.setOnClickListener {
            findNavController().navigate(R.id.action_botFragment_to_picturesFragment)
        }
        binding.editimage.setOnClickListener {
            findNavController().navigate(R.id.action_botFragment_to_imageEditFragment)
        }

    }



}





