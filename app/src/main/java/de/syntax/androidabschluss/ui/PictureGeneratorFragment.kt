package de.syntax.androidabschluss.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.databinding.FragmentPictureGeneratorBinding
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

    private lateinit var binding: FragmentPictureGeneratorBinding

    //Integrieren des Chatmodels und des Dialoges

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }
    private val viewImageDialog: Dialog by lazy {
        Dialog(requireActivity(), R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.view_image_dialog)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPictureGeneratorBinding.inflate(inflater,container,false)
        binding.styleswitch.isChecked = false
        binding.styleswitch.visibility = View.GONE

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
        binding.imageSizeRG.setOnCheckedChangeListener { group, checkedId ->
            if (binding.radioBtn3.isChecked) {
                binding.hqswitch.visibility = View.VISIBLE
            } else {
                binding.hqswitch.visibility = View.GONE
            }
        }

**/

        var maxlength = 1000

        binding.toolbarLayout2.backbutton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbarLayout2.titletexttool.text = "Picturator"

        binding.numberListACT.setAdapter(
            ArrayAdapter(
                view.context,
                android.R.layout.simple_list_item_1,
                (1..10).toList()
            )

        )

        binding.hqswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // RadioButton aktivieren, wenn der Switch aktiviert ist
                binding.radioBtn3.isChecked = true
                binding.radioBtn1.setText("1024x1024")
                binding.radioBtn2.setText("1792x1024")
                binding.radioBtn3.setText("1024x1792")
                maxlength = 4000


                binding.styleswitch.isChecked = true
                binding.styleswitch.visibility = View.VISIBLE

                binding.numberListACT.setText("1")
                binding.numberListACT.setAdapter(
                    ArrayAdapter(
                        view.context,
                        android.R.layout.simple_list_item_1,
                        (1..1).toList()
                    )
                )

            } else {
                binding.numberListACT.setAdapter(
                    ArrayAdapter(
                        view.context,
                        android.R.layout.simple_list_item_1,
                        (1..10).toList()
                    )
                )
                binding.radioBtn1.setText("256x256")
                binding.radioBtn2.setText("512x512")
                binding.radioBtn3.setText("1024x1024")
                maxlength = 1000

                binding.styleswitch.isChecked = false
                binding.styleswitch.visibility = View.GONE
            }
        }







        binding.generateBtn.setOnClickListener {
            view.context.hideKeyBoard(it)

            binding.generateBtn.visibility = View.GONE
            if (binding.edPrompt.text.toString().trim().isNotEmpty()) {
                if (binding.edPrompt.text.toString().trim().length < maxlength) {
                    Log.d( "Prompt", binding.edPrompt.text.toString().trim())
                    Log.d("numberListACT", binding.numberListACT.text.toString().trim())

                    val selectedSizeRB = view.findViewById<RadioButton>(binding.imageSizeRG.checkedRadioButtonId)
                    Log.d("selectedSizeRB", selectedSizeRB.text.toString().trim())

                    val qualityValue = if (binding.radioBtn3.isChecked && binding.hqswitch.isActivated) "standart" else "hd"
                    val styleValue = if (binding.radioBtn3.isChecked && binding.hqswitch.isChecked && binding.styleswitch.isChecked) "vivid" else "natural"
                    val modelValue = if (binding.hqswitch.isChecked) "dall-e-3" else "dall-e-2"
                    maxlength = if (binding.hqswitch.isChecked) 4000 else 1000


                    chatViewModel.createImage(
                        CreateImageRequest(
                            binding.numberListACT.text.toString().toInt(),
                            quality = qualityValue,
                            binding.edPrompt.text.toString().trim(),
                            selectedSizeRB.text.toString().trim(),
                            style = styleValue,
                            model = modelValue


                        )

                    )


                    binding.edPrompt.text = null
                } else {
                    view.context.longToastShow("The maximum length is 1000 characters.")
                }
            } else {
                view.context.longToastShow("Prompt is required")
            }
        }




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

        binding.imageRv.adapter = imageAdapter

        binding.downloadAllBtn.setOnClickListener {
            imageAdapter.currentList.map {
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect {
                when(it.status){
                    Status.LOADING -> {
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.VISIBLE
                        }
                    }
                    Status.SUCCESS -> {
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.GONE

                            imageAdapter.submitList(
                                it.data?.data
                            )
                            if (imageAdapter.currentList.isNotEmpty()){
                                binding.downloadAllBtn.visibility = View.VISIBLE
                            }else{
                                binding.downloadAllBtn.visibility = View.GONE

                            }

                        }
                    }
                    Status.ERROR -> {
                        withContext(Dispatchers.Main){
                            binding.loadingPB.visibility = View.GONE
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }

                    }
                }
            }
        }





    }








}


