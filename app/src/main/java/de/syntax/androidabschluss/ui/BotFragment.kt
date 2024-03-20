package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.databinding.FragmentBotBinding
import de.syntax.androidabschluss.utils.assistantImageList
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.viewmodel.AssistantViewModel
import java.util.UUID


class BotFragment : Fragment() {

    private var _binding: FragmentBotBinding? = null
    private val binding get() = _binding!!
    private val assistantViewModel : AssistantViewModel by lazy {
        ViewModelProvider(this)[AssistantViewModel::class.java]

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBotBinding.inflate(inflater, container, false)

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
        binding.btnGpt.setOnClickListener {
            findNavController().navigate(R.id.action_botFragment_to_gptFragment)
        }
        binding.addbutton.setOnClickListener{
            addAssistant(it)

        }

    }

    private fun addAssistant(view: View){
        val edAssistantName = EditText(view.context)
        edAssistantName.hint = "Assistantname eingeben:"
        edAssistantName.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(50, 30, 50, 30)
        textInputLayout.layoutParams = params

        textInputLayout.addView(edAssistantName)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Add a new robot")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Add"){ dialog, which ->
                val assistantName = edAssistantName.text.toString().trim()
                if (assistantName.isNotEmpty()){
                    assistantViewModel.insertRobot(
                        Assistant(
                            UUID.randomUUID().toString(),
                            assistantName,
                            assistantImageList.random()


                        )
                    )

                }else{
                    view.context.longToastShow("Abgelaufen")
                }

            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

}





