package de.syntax.androidabschluss.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.databinding.FragmentDeepLBinding
import de.syntax.androidabschluss.utils.languageFullNames
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

        binding.toolbarLayout2.titletext.setText("Translator")
        binding.toolbarLayout2.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }


        binding.targetlanglist.setAdapter(
            ArrayAdapter(
                view.context,
                R.layout.simple_list_item_1,
                languageFullNames
            )
        )


        binding.targetlanglist.setOnItemClickListener { adapterView, view, position, id ->
            val selectedLanguage = adapterView.getItemAtPosition(position) as String
            when (selectedLanguage) {
                "Arabic" -> binding.targetlang.text = "AR"
                "Bulgarian" -> binding.targetlang.text = "BG"
                "Czech" -> binding.targetlang.text = "CS"
                "Danish" -> binding.targetlang.text = "DA"
                "German" -> binding.targetlang.text = "DE"
                "Greek" -> binding.targetlang.text = "EL"
                "English" -> binding.targetlang.text = "EN"
                "English (British)" -> binding.targetlang.text = "EN-GB"
                "English (American)" -> binding.targetlang.text = "EN-US"
                "Spanish" -> binding.targetlang.text = "ES"
                "Estonian" -> binding.targetlang.text = "ET"
                "Finnish" -> binding.targetlang.text = "FI"
                "French" -> binding.targetlang.text = "FR"
                "Hungarian" -> binding.targetlang.text = "HU"
                "Indonesian" -> binding.targetlang.text = "ID"
                "Italian" -> binding.targetlang.text = "IT"
                "Japanese" -> binding.targetlang.text = "JA"
                "Korean" -> binding.targetlang.text = "KO"
                "Lithuanian" -> binding.targetlang.text = "LT"
                "Latvian" -> binding.targetlang.text = "LV"
                "Norwegian (Bokmål)" -> binding.targetlang.text = "NB"
                "Dutch" -> binding.targetlang.text = "NL"
                "Polish" -> binding.targetlang.text = "PL"
                "Portuguese" -> binding.targetlang.text = "PT"
                "Portuguese (Brazilian)" -> binding.targetlang.text = "PT-BR"
                "Portuguese (Portugal)" -> binding.targetlang.text = "PT-PT"
                "Romanian" -> binding.targetlang.text = "RO"
                "Russian" -> binding.targetlang.text = "RU"
                "Slovak" -> binding.targetlang.text = "SK"
                "Slovenian" -> binding.targetlang.text = "SL"
                "Swedish" -> binding.targetlang.text = "SV"
                "Turkish" -> binding.targetlang.text = "TR"
                "Ukrainian" -> binding.targetlang.text = "UK"
                "Chinese (simplified)" -> binding.targetlang.text = "ZH"
            }
        }


        binding.btntranslate.setOnClickListener {
            viewModel.translateText(binding.targetlang.text.toString(), binding.textToTranslate.text.toString())
        }

        viewModel.translation.observe(viewLifecycleOwner) { translation ->
            binding.translatedtext.setText(translation)
        }
    }
}
