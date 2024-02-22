package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentBotBinding
import de.syntax.androidabschluss.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BotFragment : Fragment() {

    private var _binding: FragmentBotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_botFragment_to_mainFragment)
        }
        binding.sendButton.setOnClickListener {
            val inputMessage: String = binding.inputText.text.toString()

            if (inputMessage.isNotEmpty()) {
                makeRequestToChatGpt(inputMessage)
            } else {
                Toast.makeText(requireContext(), "Eingabe machen", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.apiResponse.collect { response ->
                if (response != null) {
                    binding.chatView.text = response
                }
            }
        }
    }

    private fun makeRequestToChatGpt(message: String){
        lifecycleScope.launch {
            viewModel.getApiResponse(message)
            viewModel.apiResponse.onEach { response ->
                if (response != null){
                    binding?.chatView?.text = response
                }
            }.launchIn(lifecycleScope)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





