package de.syntax.androidabschluss.ui


import BotViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.syntax.androidabschluss.databinding.FragmentBotBinding

class BotFragment : Fragment() {

    private lateinit var binding: FragmentBotBinding
    private lateinit var viewModel: BotViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBotBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(BotViewModel::class.java)

        setupUI()

        return binding.root
    }

    private fun setupUI() {
        // Beobachten der Antwort und Aktualisieren der UI
        viewModel.response.observe(viewLifecycleOwner) { response ->
            // Hier Code zum Hinzufügen der Antwort zur UI, z.B. in einem TextView
        }

        binding.btnSend.setOnClickListener {
            val userInput = binding.editTextTextPersonName.text.toString()
            viewModel.getResponse(userInput)
            binding.editTextTextPersonName.text.clear()
        }
    }
}
