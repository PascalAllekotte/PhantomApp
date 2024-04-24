package de.syntax.androidabschluss.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.data.local.getDatabaseTerm
import de.syntax.androidabschluss.data.model.open.TermItem
import de.syntax.androidabschluss.databinding.FragmentTermDetailDetailBinding
import de.syntax.androidabschluss.utils.longToastShow
import kotlinx.coroutines.launch
import java.util.Calendar


class TermDetailDetailFragment : Fragment() {

    private lateinit var binding : FragmentTermDetailDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermDetailDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout1.titletext.setText("Term")
        binding.toolbarLayout1.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.date.setOnClickListener{
            showDate()
        }


        // Eckdaten vom xml

        binding.addbutton.setOnClickListener {
            if (binding.termcontent.text?.isNotEmpty() == true && binding.date.text.isNotEmpty() == true){
                addTermItem()
                context?.longToastShow("Termin hinzugefügt")
            } else{
                context?.longToastShow("Bitte erst Eingabe machen")
            }


        }


    }

    //
    private fun showDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth.${month + 1}.$year"
            binding.date.text = selectedDate
        }, year, month, day).show()
    }


    fun addTermItem(){
        val termtext = binding.termcontent.text.toString()
        val termdate = binding.date.text.toString()

        if (termdate.isNotEmpty() && termtext.isNotEmpty()){
            val parts = termdate.split(".")
            val day = parts[0]
            val month = parts[1]
            val year = parts[2]
            val sortableDate = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"

            val newTermItem = TermItem(
                termDate = sortableDate,
                termText = termtext
            )

            lifecycleScope.launch{
                val db = getDatabaseTerm(requireContext())
                db.termDatabaseDao().insert(newTermItem)
            }
        }
    }
}


