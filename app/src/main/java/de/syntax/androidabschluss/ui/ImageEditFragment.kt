package de.syntax.androidabschluss.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.databinding.FragmentImageEditBinding
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.appSettingOpen
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
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf()  // Keine Berechtigungen erforderlich für Android 12 und höher
    } else {
        arrayListOf(  // Berechtigungen für den Zugriff auf externen Speicher für Android-Versionen vor 12
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentImageEditBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titletext.setText("Editor")
        return binding.root
    }


    // Späte Initialisierung von URIs für das Original- und das maskierte Bild
    private lateinit var originalImageUri: Uri
    private lateinit var maskedImageUri: Uri

    // ActivityResultLauncher zur Auswahl eines Bildes aus der Galerie oder anderen Quellen
    private val singlePhotoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            try {
                uri?.let {
                    // Lädt das Bild aus der URI in einen Bitmap
                    val savedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)

                    // Setzt das ausgewählte Bild in einer benutzerdefinierten ImageView
                    binding.image.setInitialBitmap(savedBitmap)

                    // Zeigt das Bild in einer weiteren ImageView an
                    binding.imagetra.setImageBitmap(savedBitmap)
                    binding.imagetra.visibility = View.VISIBLE
                    binding.imagefilter.visibility = View.VISIBLE

                    // Erstellt ein Bitmap aus der Custom View (zur weiteren Bearbeitung oder Speicherung)
                    val bitmap = getBitmapFromView(binding.image, binding.image.myHeight, binding.image.myWidth)

                    // Speichert das erstellte Bitmap lokal in einer Datei
                    val maskedFile = File(requireContext().filesDir, "photos.png")
                    FileOutputStream(maskedFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    // Hilfsfunktion zur Erstellung einer URI für ein Bild, basierend auf einem Dateinamen
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

        // Rückgängig-Button, der die letzte Aktion im Zeichnungsbereich rückgängig macht
        binding.undo.setOnClickListener {
            binding.image.undo()
        }

        // SeekBar-Listener, der auf Änderungen der Strichstärke reagiert
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Aktualisiert die Strichstärke im Zeichenbereich
                val strokeWidth = progress.toFloat() // Umwandlung des Fortschritts in Float
                binding.image.updateStrokeWidth(strokeWidth)
                binding.masksize.text = strokeWidth.toString() // Zeigt den aktuellen Wert an
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // Rückkehr-Button in der Toolbar, führt zurück zur vorherigen Ansicht
        binding.toolbarLayout.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Initialisiert URIs für originale und maskierte Bilder
        originalImageUri = view.context.createImageUri("photos.png")
        maskedImageUri = view.context.createImageUri("photos-masked.png")

        // Button zum Auswählen eines Bildes aus der Galerie
        binding.btnSelect.setOnClickListener {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // Button zum Starten der Bildbearbeitung mit vom Nutzer eingegebenem Text
        binding.btnedit.setOnClickListener {
            if (binding.edPrompt.text.toString().trim().isNotEmpty()) {
                // Erstellt ein Bitmap von der aktuellen Zeichenansicht
                val bitmap = getBitmapFromView(binding.image, binding.image.myHeight, binding.image.myWidth)
                // Speichert das bearbeitete Bild lokal
                val maskedFile = File(requireContext().filesDir, "photos-masked.png")
                FileOutputStream(maskedFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
                }
                // Startet den Bildbearbeitungsprozess über das ViewModel
                chatViewModel.createImageEdit(
                    binding.edPrompt.text.toString().trim(),
                    File(requireContext().filesDir,"photos.png"),
                    maskedFile,
                    2,
                    "1024x1024"
                )
            } else {
                // Zeigt eine Fehlermeldung an, wenn kein Text eingegeben wurde
                view.context.longToastShow("Message is Required")
            }
        }


        val loadImagein = viewImageDialog.findViewById<ImageView>(R.id.loadImage3)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)

        cancelBtn.setOnClickListener {
            viewImageDialog.dismiss()
        }

        // Initialisiert einen Bildadapter, der auf Klick ein Dialogfenster zeigt und das Bild lädt
        val imageEditAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImagein)
                .load(data.url)  // Lädt das Bild über URL
                .placeholder(R.drawable.ic_placeholder)  // Zeigt ein Platzhalterbild während des Ladens
                .into(loadImagein)

            downloadBtn.setOnClickListener {
                if (it.context.checkMultiplePermission()) {
                    it.context.download(data.url)
                } else {
                    appSettingOpen(it.context)
                }
            }
        }

        binding.imageRv.adapter = imageEditAdapter



        // Listener für den Download-All-Button
        binding.downloadAllBtn.setOnClickListener {
            if (it.context.checkMultiplePermission()) {
                // Startet den Download für alle Bilder in der Liste
                imageEditAdapter.currentList.map { list ->
                    it.context.download(list.url)
                }
            } else {
                appSettingOpen(it.context)
            }
        }
        // Nutzt Coroutines, um Bildlisten asynchron zu verarbeiten und UI-Aktualisierungen zu handhaben
        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect {
                when(it.status) {
                    Status.LOADING -> {
                        // Zeigt Ladeanimation im Hauptthread
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.VISIBLE
                        }
                    }
                    Status.SUCCESS -> {
                        // Versteckt Ladeanimation und aktualisiert die Bildliste im Hauptthread
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.GONE
                            imageEditAdapter.submitList(it.data?.data)
                            Log.d("imageList", it.data?.data.toString())
                            // Zeigt oder verbirgt den "Download All"-Button basierend auf der Liste
                            if (imageEditAdapter.currentList.isNotEmpty()) {
                                binding.downloadAllBtn.visibility = View.VISIBLE
                                binding.dowloadall.visibility= View.VISIBLE
                            } else {
                                binding.downloadAllBtn.visibility = View.GONE
                                binding.dowloadall.visibility= View.GONE
                            }


                        }
                    }
                    Status.ERROR -> {
                        // Versteckt Ladeanimation und zeigt Fehlermeldung im Hauptthread
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.GONE
                            it.message?.let { message -> view.context.longToastShow(message) }
                        }
                    }
                }
            }
        }
    }
    // Erzeugt ein Bitmap aus einer View, indem es deren visuellen Inhalt aufzeichnet
    fun getBitmapFromView(view: View, myHeight: Int, myWidth: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    // Funktion zum Erzeugen einer zufälligen Zeichenkette für Dateinamen
    private fun getRandomString(): String {
        val allowedChar = ('A'..'Z') + ('a'..'z') + (0..9)
        return (1..7)
            .map { allowedChar.random() }
            .joinToString("")
    }

    // Funktion zum Herunterladen einer Datei über URL
    private fun Context.download(url: String) {
        // Prüft, ob der Download-Ordner existiert, und erstellt ihn, falls nicht vorhanden
        val folder = File(Environment.getExternalStorageDirectory().toString() + "/Download/Image")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        longToastShow("Download Started")  // Zeigt Benachrichtigung für den Downloadstart

        // Erstellt einen zufälligen Dateinamen
        val fileName = getRandomString() + ".jpg"

        // Verwendet den DownloadManager für den Download
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
        request.setTitle(fileName)  // Setzt den Titel der Download-Benachrichtigung
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Image/$fileName")
        downloadManager.enqueue(request)  // Startet den Download
    }

    private fun Context.checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)  // Fügt fehlende Berechtigungen zur Liste hinzu
            }
        }
        return listPermissionNeeded.isEmpty()  // Gibt true zurück, wenn alle Berechtigungen erteilt wurden
    }

}

//A striking 3D render of Bart Simpson in a full-body shot, wearing his iconic yellow shirt, blue shorts, and red sneakers. He dons a pair of cool black shaded sunglasses, accentuating his rebellious persona. The background is a cinematic, high-contrast urban setting, with graffiti-covered walls and a neon sign. The overall atmosphere is edgy and fashionable, capturing the essence of Bart's unique style., cinematic, illustration, 3d render, fashion bart Simpson, full body shot, wearing typical outfit, wearing black shaded sunglasses, 3/4 angle shot, illustration, 3d render, cinematic, fashion
//A breathtaking cinematic illustration of a dreamlike, dark fantasy world inspired by the surrealism of Salvador Dali and René Magritte. A tiny man-hatter the size of an ant in a top hat sits on a floating clock drinking tea, a rabbit and a mouse, a Cheshire cat in a top hat and a melting spoon drips into a cup. Microscopic details such as intricate gears and fine filigree create a sense of depth and complexity. The entire composition is filled with bright colors, inviting viewers to plunge into a whimsical, fairy-tale landscape and explore its mysterious secrets. This versatile 3D render can be adapted to a variety of formats, including magazine covers, posters and cinematic photography, making it a truly exciting and creative piece of art. 3D rendering, cinematic, illustration, dark fantasy., 3d render, illustration, cinematic, vibrant, conceptual art, architecture