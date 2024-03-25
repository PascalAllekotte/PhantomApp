package de.syntax.androidabschluss.ui
/**
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.databinding.FragmentPictureGeneratorBinding
import de.syntax.androidabschluss.response.CreateImageRequest
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.appSettingOpen
import de.syntax.androidabschluss.utils.hideKeyBoard
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.utils.setupDialog
import de.syntax.androidabschluss.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class PictureGeneratorFragmentcopy : Fragment() {
    private lateinit var binding: FragmentPictureGeneratorBinding

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val viewImageDialog: Dialog by lazy {
        Dialog(requireActivity(), R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.view_image_dialog)
        }
    }
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf()
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    private fun Context.checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            return false
        }
        return true
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


        val imageRV = view.findViewById<RecyclerView>(R.id.imageRv)
        val loadingPB = view.findViewById<ProgressBar>(R.id.progressbar)
        val downloadAllBtn = view.findViewById<Button>(R.id.downloadAllBtn)

        val loadImage = viewImageDialog.findViewById<ImageView>(R.id.loadImage)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)


        cancelBtn.setOnClickListener {
            viewImageDialog.dismiss()
        }


        //obere teil passt
        val imageAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImage)
                .load(data.url)
                .placeholder(R.drawable.katze)
                .into(loadImage)

            downloadBtn.setOnClickListener {
                if (it.context.checkMultiplePermission()) {
                    it.context.download(data.url)
                } else {
                    appSettingOpen(it.context)
                }


            }
        }

        binding.imageRv.adapter = imageAdapter
        binding.downloadAllBtn.setOnClickListener {
            if (it.context.checkMultiplePermission()) {
                imageAdapter.currentList.map { list->
                    it.context.download(list.url)
                }
            } else {
                appSettingOpen(it.context)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect {
                when (it.status) {
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

                            if (imageAdapter.currentList.isNotEmpty()) {
                                downloadAllBtn.visibility = View.VISIBLE
                            } else {
                                loadingPB.visibility = View.GONE
                            }
                        }
                    }

                    Status.ERROR -> {
                        withContext(Dispatchers.Main) {
                            loadingPB.visibility = View.GONE
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
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
                            Log.d(
                                "selectedSizeRB",
                                binding.root.findViewById<RadioButton>(selectedSizeRB).text.toString()
                                    .trim()
                            )

                            chatViewModel.createImageRequest(
                                CreateImageRequest(
                                    binding.anzahlListe.text.toString().toInt(),
                                    binding.etInput.text.toString().trim(),
                                    binding.root.findViewById<RadioButton>(selectedSizeRB).text.toString()
                                        .trim()

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





            }
        }
    }

    private fun getRandomString() : String {
        val allowedChar = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..7)
            .map { allowedChar.random()}
            .joinToString { "" }
    }
    private fun Context.download(url: String) {
        val folder = File(
            Environment.getExternalStorageDirectory().toString() + "/Download/Image"
        )
        if (!folder.exists()) {
            folder.mkdirs()
        }
        longToastShow("Download Started")
        val fileName = getRandomString() + ".jpg"

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE
        )
        request.setTitle(fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "Image/$fileName"
        )
        downloadManager.enqueue(request)

    }
                }

**/