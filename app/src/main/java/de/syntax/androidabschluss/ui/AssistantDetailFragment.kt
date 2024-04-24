package de.syntax.androidabschluss.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentAssistantDetailBinding
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class AssistantDetailFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    private lateinit var binding: FragmentAssistantDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAssistantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Zurück-Navigation
        binding.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }

        // Textkopierfunktion für Chat-Ansicht
        binding.chatView.setOnLongClickListener {
            val textToString = binding.chatView.text.toString()
            val clipboard = ContextCompat.getSystemService(it.context, ClipboardManager::class.java) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", textToString)
            clipboard.setPrimaryClip(clip)
            context?.longToastShow("Text copied to clipboard")
            true
        }

        // Senden-Button und Nachrichtenverarbeitung
        binding.sendButton.setOnClickListener {
            val inputMessage: String = binding.inputText.text.toString()
            val vibrationAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anima)
            it.startAnimation(vibrationAnimation)
            if (inputMessage.isNotEmpty()) {
                makeRequestToChatGpt(inputMessage)
            } else {
                context?.longToastShow("Eingabe machen")
            }
        }

        // API-Antwort beobachten und anzeigen
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.apiResponse.collect { response ->
                if (response != null) {
                    binding.chatView.text = response
                }
            }
        }
    }

    // Funktion zum Senden von Nachrichten und Aktualisieren der Chat-Ansicht
    private fun makeRequestToChatGpt(message: String){
        lifecycleScope.launch {
            viewModel.getApiResponse(message)
            viewModel.apiResponse.onEach { response ->
                if (response != null){
                    binding.chatView.text = response
                }
            }.launchIn(lifecycleScope)
        }
    }
}