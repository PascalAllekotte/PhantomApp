package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.data.local.getDatabaseNote
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.databinding.FragmentNoteDetailDetailBinding
import de.syntax.androidabschluss.utils.longToastShow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteDetailDetailFragment : Fragment() {

    private lateinit var binding : FragmentNoteDetailDetailBinding

    private var currentdate : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        currentdate = sdf.format(Date())

        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addbutton.setOnClickListener {
            if (binding.noteTitle.text?.isNotEmpty() == true && binding.notetext.text?.isNotEmpty() == true){
                addnoteItem()
                context?.longToastShow("Notiz hinzugefügt")
            } else {
                context?.longToastShow("Bitte erst Eingabe machen!")
            }


        }

        binding.toolbarLayout.titletexttool.setText("$currentdate")


    }
    //ToDo wenn notetitel vorhanden updaten und + zu einem haken ändern
    fun addnoteItem() {
        val title = binding.noteTitle.text.toString()
        val content = binding.notetext.text.toString()
        val dateTime = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

        if(title.isNotEmpty() && content.isNotEmpty()){
            val newNoteItem = NoteItem(
                title = title,
                content = content,
                dateTime = dateTime.format(Date())
            )
            // lifecycle
            lifecycleScope.launch {
                val dp = getDatabaseNote(requireContext())
                dp.noteDataBaseDao().insert(newNoteItem)
            }
        }


    }




}