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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.databinding.FragmentPictureGeneratorBinding
import de.syntax.androidabschluss.response.CreateImageRequest
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.hideKeyBoard
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.utils.setupDialog
import de.syntax.androidabschluss.viewmodel.ChatViewModel
import de.syntax.androidabschluss.viewmodel.ImageGenerationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PictureGeneratorFragment : Fragment() {
    private lateinit var binding: FragmentPictureGeneratorBinding
    private val imageGenerationViewModel: ImageGenerationViewModel by viewModels()

    private val chatViewModel : ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val viewImageDialog : Dialog by lazy {
        Dialog(requireActivity(),R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.view_image_dialog)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureGeneratorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadImg = viewImageDialog.findViewById<ImageView>(R.id.loadImage)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)

        cancelBtn.setOnClickListener{
            viewImageDialog.dismiss()
        }

        val imageAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImg).load(data.url)
                .placeholder(R.drawable.katze)
                .into(loadImg)
            downloadBtn.setOnClickListener {


            }
        }

        binding.imageRv.adapter = imageAdapter
        binding.downloadAllBtn.setOnClickListener{
            imageAdapter.currentList.map {
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect{
                when(it.status){
                    Status.LOADING ->
                        withContext(Dispatchers.Main){
                            binding.progressbar.visibility = View.VISIBLE
                }
                    Status.SUCCESS ->
                        withContext(Dispatchers.Main){
                            binding.progressbar.visibility = View.GONE

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
                    Status.ERROR ->
                        withContext(Dispatchers.Main){
                            binding.progressbar.visibility = View.VISIBLE

        }
            }

        }

        downloadAllBtn.setOnClickListener {
            imageAdapter.currentList.map {
                // Der Code für das Verarbeiten der aktuellen Liste fehlt hier.
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect { it: Resource<ImageResponse> ->
                when(it.status) {
                    // Der Code für das Status-Handling fehlt hier.
                }
            }
        }


        binding.anzahlListe.setAdapter(
            ArrayAdapter(
                view.context,
                android.R.layout.simple_list_item_1,
                (1..10).toList()
            )
        )

        binding.btGenerate.setOnClickListener {
            view.context.hideKeyBoard(it)
            if (binding.etInput.text.toString().trim().isNotEmpty()) {
                if (binding.etInput.text.toString().trim().length < 1000) {
                    Log.d("etinput", binding.etInput.text.toString().trim())
                    Log.d("anzahlListe", binding.anzahlListe.text.toString().trim())

                    val selectedSizeRB = binding.imageGrE.checkedRadioButtonId
                    Log.d("selectedSizeRB",
                        binding.root.findViewById<RadioButton>(selectedSizeRB).text.toString().trim()
                    )

                    chatViewModel.createImageRequest(
                        CreateImageRequest(
                            binding.anzahlListe.text.toString().toInt(),
                            binding.etInput.text.toString().trim(),
                            binding.root.findViewById<RadioButton>(selectedSizeRB).text.toString().trim()

                        )

                    )
                    //      binding.etInput.text = null

                } else {
                    view.context.longToastShow("auswahl machen")

                }

            } else {
                view.context.longToastShow("auswahl machen")
            }
        }



        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()


        }

        imageGenerationViewModel.imageUrl.observe(viewLifecycleOwner) { imgUrl ->
            if (imgUrl != null) {
                loadImage(imgUrl)
            } else {
                // Handle error
            }
        }

        imageGenerationViewModel.isInProgress.observe(viewLifecycleOwner) { isInProgress ->
            setInProgress(isInProgress)
        }

        binding.btGenerate.setOnClickListener {
            val text = binding.etInput.text.toString()
            if (text.isEmpty()) {
                binding.etInput.setError("No Empty Text")
            } else {
                imageGenerationViewModel.generateImage(text)
            }
        }
    }

    private fun loadImage(imgUrl: String) {
        Picasso.get().load(imgUrl).into(binding.imageView)
        binding.imageView.visibility = View.VISIBLE
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.progressbar.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.btGenerate.visibility = if (inProgress) View.GONE else View.VISIBLE
    }
}