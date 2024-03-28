package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import de.syntax.androidabschluss.databinding.FragmentDeepLBinding
import de.syntax.androidabschluss.viewmodel.TranslationViewModel


class DeepLFragment : Fragment() {

    private lateinit var binding: FragmentDeepLBinding
    private val viewModel: TranslationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeepLBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btntranslate.setOnClickListener {
            viewModel.translateText(binding.textToTranslate.text.toString())
        }

        viewModel.translation.observe(viewLifecycleOwner) { translation ->
            binding.translatedtext.setText(translation)
        }
    }
}
