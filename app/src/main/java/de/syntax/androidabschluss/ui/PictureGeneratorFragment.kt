package de.syntax.androidabschluss.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.response.CreateImageRequest
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.hideKeyBoard
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.utils.setupDialog
import de.syntax.androidabschluss.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PictureGeneratorFragment : Fragment() {

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }
    private val viewImageDialog: Dialog by lazy {
        Dialog(requireActivity(), R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.view_image_dialog)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val imageRV = view.findViewById<RecyclerView>(R.id.imageRv)
        val loadingPB = view.findViewById<ProgressBar>(R.id.loadingPB)
        val downloadAllBtn = view.findViewById<Button>(R.id.downloadAllBtn)

        val loadImagein = viewImageDialog.findViewById<ImageView>(R.id.loadImage3)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)


        cancelBtn.setOnClickListener {
            viewImageDialog.dismiss()
        }

        val imageAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImagein)
                .load(data.url)
                .placeholder(R.drawable.ic_placeholder)
                .into(loadImagein)

            downloadBtn.setOnClickListener {

            }
        }

        imageRV.adapter = imageAdapter

        downloadAllBtn.setOnClickListener {
            imageAdapter.currentList.map {
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect {
                when(it.status){
                    Status.LOADING -> {
                        withContext(Dispatchers.Main) {
                            loadingPB.visibility = View.VISIBLE
                        }
                    }
                    Status.SUCCESS -> {
                        withContext(Dispatchers.Main) {
                            loadingPB.visibility = View.GONE

                            imageAdapter.submitList(
                                it.data?.data
                            )
                            if (imageAdapter.currentList.isNotEmpty()){
                                downloadAllBtn.visibility = View.VISIBLE
                            }else{
                                downloadAllBtn.visibility = View.GONE

                            }

                        }
                    }
                    Status.ERROR -> {
                        withContext(Dispatchers.Main){
                            loadingPB.visibility = View.GONE
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }

                    }
                }
            }
        }





    }







    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_picture_generator, container, false)
        val toolbarView = view.findViewById<View>(R.id.toolbarLayout2)


        val closeImage = toolbarView.findViewById<ImageView>(R.id.backbutton)
        closeImage.setOnClickListener {
            findNavController().navigateUp()
        }

        val titleTxt = toolbarView.findViewById<TextView>(R.id.titletexttool)
        titleTxt.text = "ChatGPT App"

        val numberListACT = view.findViewById<AutoCompleteTextView>(R.id.numberListACT)
        numberListACT.setAdapter(
            ArrayAdapter(
                view.context,
                android.R.layout.simple_list_item_1,
                (1..10).toList()
            )
        )

        val imageSizeRG = view.findViewById<RadioGroup>(R.id.imageSizeRG)
        val edPrompt = view.findViewById<EditText>(R.id.edPrompt)

        val generateBtn = view.findViewById<Button>(R.id.generateBtn)
        generateBtn.setOnClickListener {
            view.context.hideKeyBoard(it)

            if (edPrompt.text.toString().trim().isNotEmpty()) {
                if (edPrompt.text.toString().trim().length < 1000) {
                    Log.d( "Prompt", edPrompt.text.toString().trim())
                    Log.d("numberListACT", numberListACT.text.toString().trim())

                    val selectedSizeRB = view.findViewById<RadioButton>(imageSizeRG.checkedRadioButtonId)
                    Log.d("selectedSizeRB", selectedSizeRB.text.toString().trim())

                    chatViewModel.createImage(
                        CreateImageRequest(
                            numberListACT.text.toString().toInt(),
                            edPrompt.text.toString().trim(),
                            selectedSizeRB.text.toString().trim()
                        )
                    )


                    edPrompt.text = null
                } else {
                    view.context.longToastShow("The maximum length is 1000 characters.")
                }
            } else {
                view.context.longToastShow("Prompt is required")
            }
        }



        return view
    }
}


