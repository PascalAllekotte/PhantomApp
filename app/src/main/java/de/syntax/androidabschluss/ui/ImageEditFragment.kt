package de.syntax.androidabschluss.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.databinding.FragmentImageEditBinding
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.utils.setupDialog
import de.syntax.androidabschluss.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class ImageEditFragment : Fragment() {

    private lateinit var binding: FragmentImageEditBinding
    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }
    private val viewImageDialog: Dialog by lazy {
        Dialog(requireActivity(), R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.view_image_dialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentImageEditBinding.inflate(inflater, container, false)
        return binding.root
    }


    private lateinit var originalImageUri: Uri
    private lateinit var maskedImageUri: Uri
    private val singlePhotoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            try {
                uri?.let {

                    val savedBitmap =
                        MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)

                    binding.image.setInitialBitmap(savedBitmap)

                    binding.imagetra.setImageBitmap(savedBitmap)
                    binding.imagetra.visibility = View.VISIBLE
                    binding.imagefilter.visibility = View.VISIBLE

                    binding.imagetra.visibility = View.VISIBLE

                    val bitmap = getBitmapFromView(binding.image,binding.image.myHeight,binding.image.myWidth)

                    val maskedFile = File(requireContext().filesDir, "photos.png")
                    FileOutputStream(maskedFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun Context.createImageUri(fileName: String): Uri {
        val image = File(filesDir, fileName)
        return FileProvider.getUriForFile(
            this,
            "${packageName}.FileProvider",
            image
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.undo.setOnClickListener {
            binding.image.undo()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update die Strichstärke basierend auf dem Wert des SeekBars
                val strokeWidth = progress.toFloat() // Convert progress to float
                binding.image.updateStrokeWidth(strokeWidth)
                binding.masksize.text = strokeWidth.toString()


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.mouse)
//        binding.image.setBitmap(bitmap)
        originalImageUri = view.context.createImageUri("photos.png")
        maskedImageUri = view.context.createImageUri("photos-masked.png")
        binding.btnSelect.setOnClickListener {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

            )



        }
        binding.btnedit.setOnClickListener {
            if (binding.edPrompt.text.toString().trim().isNotEmpty()) {
                val bitmap = getBitmapFromView(binding.image,binding.image.myHeight,binding.image.myWidth)
                val maskedFile = File(requireContext().filesDir, "photos-masked.png")
                FileOutputStream(maskedFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
                }
                chatViewModel.createImageEdit(
                    binding.edPrompt.text.toString().trim(),
                    File(requireContext().filesDir,"photos.png"),
                    maskedFile,
                    2,
                    "1024x1024"
                )

            } else {
                view.context.longToastShow("Message is Required")
            }
        }


        val loadImagein = viewImageDialog.findViewById<ImageView>(R.id.loadImage3)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)

        cancelBtn.setOnClickListener {
            viewImageDialog.dismiss()
        }

        val imageEditAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImagein)
                .load(data.url)
                .placeholder(R.drawable.ic_placeholder)
                .into(loadImagein)

            downloadBtn.setOnClickListener {

            }
        }
        binding.imageRv.adapter = imageEditAdapter

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
//
                            imageEditAdapter.submitList(
                                it.data?.data
                            )


                            Log.d("imageList",it.data?.data.toString())

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
    fun getBitmapFromView(view: View, myHeight: Int, myWidth: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
            myWidth, myHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}

