package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import de.syntax.androidabschluss.adapter.AssistantAdapter
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.databinding.FragmentAssistantsBinding
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.StatusResult
import de.syntax.androidabschluss.utils.assistantImageList
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.viewmodel.AssistantViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID


class AssistantsFragment : Fragment() {
    private lateinit var binding: FragmentAssistantsBinding
    private val assistantViewModel: AssistantViewModel by lazy {
        ViewModelProvider(this)[AssistantViewModel::class.java]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAssistantsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**  binding.backbutton.setOnClickListener {
        findNavController().popBackStack()
        }
         **/

        binding.toolbarLayout.backbutton.setOnClickListener{
            findNavController().popBackStack()

        }
        binding.addbutton.setOnClickListener {
            addAssistant(it)
        }


        val assistantAdapter = AssistantAdapter { type, position, assistant ->

            when (type) {
                "delete" -> {
                    assistantViewModel.deleteAssistantUsingId(assistant.assistantId)

                }

                "update" -> {
                    updateAssistant(view, assistant)
                }

                else -> {
                    val action = AssistantsFragmentDirections.actionAssistantsFragmentToGptFragment(
                        assistant.assistantId,
                        assistant.assistantImg,
                        assistant.assistantName
                    )
                    findNavController().navigate(action)
                }
            }
        }
        binding.assistantRv.adapter = assistantAdapter
        assistantAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.assistantRv.smoothScrollToPosition(positionStart)
            }

        })
        callGetAssistantList(assistantAdapter, view)
        assistantViewModel.getAssistantList()
        statusCallback(view)
    }

    private fun callGetAssistantList(assistantAdapter: AssistantAdapter, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            assistantViewModel
                .assistantStatusFlow
                .collectLatest {
                    when (it.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            it.data?.collect { assistantList ->
                                assistantAdapter.submitList(assistantList)
                            }
                        }

                        Status.ERROR -> {
                            it.message?.let { it1 ->
                                view.context.longToastShow(it1)
                            }
                        }
                    }
                }
        }
    }






    private fun statusCallback(view: View) {
        assistantViewModel
            .statusLiveData
            .observe(viewLifecycleOwner) {
                if (it != null){
                when (it.status) {
                    Status.LOADING -> { }
                    Status.SUCCESS -> {
                        when (it.data as StatusResult) {
                            StatusResult.Added -> {
                                Log.d("StatusResult", "Added")
                            }

                            StatusResult.Updated ->{
                                Log.d("StatusResult", "Updated")

                            }
                            StatusResult.Deleted ->{
                                Log.d("StatusResult", "Deleted")

                            }
                        }
                        it.message?.let { it1 -> view.context.longToastShow(it1) }
                    }
                    Status.ERROR -> {

                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        assistantViewModel.clearStatusLiveData()
    }

    private fun updateAssistant(view: View, assistant: Assistant){
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
            .setTitle("updated assistant")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Update"){ dialog, which ->
                val assistantName = edAssistantName.text.toString().trim()
                if (assistantName.isNotEmpty()){
                    assistantViewModel.updateAssistant(
                        Assistant(
                            assistant.assistantId,
                            assistantName,
                            assistant.assistantImg


                        )
                    )

                }else{
                    view.context.longToastShow("Abgelaufen")
                }

            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
        edAssistantName.setText(assistant.assistantName)
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
            .setTitle("Add a new assistant")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Add"){ dialog, which ->
                val assistantName = edAssistantName.text.toString().trim()
                if (assistantName.isNotEmpty()){
                    assistantViewModel.insertAssistant(
                        Assistant(
                            UUID.randomUUID().toString(),
                            assistantName,
                            (assistantImageList.indices).random()


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
