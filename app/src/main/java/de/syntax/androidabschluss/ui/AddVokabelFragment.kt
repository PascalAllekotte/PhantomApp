package de.syntax.androidabschluss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.MainActivity
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.local.VokabelDataBase
import de.syntax.androidabschluss.adapter.local.getDatabase
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.FragmentAddVokabelBinding
import kotlinx.coroutines.launch


class AddVokabelFragment : Fragment() {

private lateinit var binding: FragmentAddVokabelBinding
    lateinit var database: VokabelDataBase



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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backbutton.setOnClickListener{
            findNavController().popBackStack()

        }


        binding.btnAdd.setOnClickListener {
            addVocabItem()
        }

    }



    private fun addVocabItem() {
        val language = binding.etLanguage.text.toString()
        val vokabel = binding.etVokabel.text.toString()
        val language2 = binding.etLanguage2.text.toString()
        val vokabel2 = binding.etVokabel2.text.toString()

        if (language.isNotEmpty() && vokabel.isNotEmpty() && language2.isNotEmpty() && vokabel2.isNotEmpty()) {
            val newItem = VocabItem(
                language = language,
                language2 = language2,
                translation = vokabel,
                translation2 = vokabel2
            )

            // Using Coroutine to perform database operation on a background thread
            lifecycleScope.launch {
                // Accessing the database using getDatabase function
                val db = getDatabase(requireContext())
                db.vokabelDataBaseDao().insert(newItem)
            }
        } else {
            // Handle error, show message to user
        }
    }
}