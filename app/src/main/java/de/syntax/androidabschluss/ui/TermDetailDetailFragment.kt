package de.syntax.androidabschluss.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.databinding.FragmentTermDetailDetailBinding
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

        binding.addbutton.setOnClickListener {
            binding.termtext.text = binding.editText.text
            binding.termdate.text = binding.date.text
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

    }


