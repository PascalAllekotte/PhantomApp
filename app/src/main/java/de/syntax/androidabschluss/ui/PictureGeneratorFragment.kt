package de.syntax.androidabschluss.ui

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
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ImageAdapter
import de.syntax.androidabschluss.data.model.open.Request.CreateImageRequest
import de.syntax.androidabschluss.databinding.FragmentPictureGeneratorBinding
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
    // Definiert eine Liste von Berechtigungen, die abhängig von der Android-Version angefordert werden sollen
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf()  // Keine Berechtigungen erforderlich für Android 12 und höher
    } else {
        arrayListOf(  // Berechtigungen für den Zugriff auf externen Speicher für Android-Versionen vor 12
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    // Überprüft, ob alle benötigten Berechtigungen erteilt wurden
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

    // Inflates das Layout für dieses Fragment und initialisiert UI-Komponenten
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPictureGeneratorBinding.inflate(inflater, container, false)
        binding.styleswitch.isChecked = false  // Initialisiert den Schalter als nicht aktiviert
        binding.styleswitch.visibility = View.GONE  // Verbirgt den Schalter im UI

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
        the right side more epic call of duty style game. A striking 3D profile picture featuring "Maly_45" prominently in the center, surrounded by a brilliantly crafted gaming character of call of duty. The character is  showcasing a dark, aggressive, and almost animalistic and epic feeling. The character is intricately detailed with sparks and realistic fur textures, bringing the figure to life. The background is enhanced by volumetric lighting and ray tracing, creating a complex and immersive environment that encapsulates both the dark and playful worlds. The overall effect is a visually stunning and conceptually captivating representation of the gaming universe. "Maly_45" , not to many colors

**/

        // Definiert eine Variable für die maximale Textlänge
        var maxlength = 1000

// Setzt einen Click-Listener auf den Rück-Button, der die aktuelle Ansicht schließt
        binding.toolbarLayout2.backbutton.setOnClickListener {
            findNavController().navigateUp()
        }

// Setzt den Titel des Toolbars
        binding.toolbarLayout2.titletexttool.text = "Picturator"

// Initialisiert den AutoCompleteTextView mit Zahlen von 1 bis 10
        binding.numberListACT.setAdapter(
            ArrayAdapter(
                view.context,
                android.R.layout.simple_list_item_1,
                (1..10).toList()
            )
        )

// Event-Listener für den High-Quality-Switch
        binding.hqswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Setzt die Radiobuttons auf höhere Auflösungen, wenn HQ aktiviert ist
                binding.radioBtn3.isChecked = true
                binding.radioBtn1.setText("1024x1024")
                binding.radioBtn2.setText("1792x1024")
                binding.radioBtn3.setText("1024x1792")
                maxlength = 4000  // Erhöht die maximale Textlänge für HQ-Modus

                // Zeigt zusätzliche UI-Elemente an, wenn HQ-Modus aktiviert ist
                binding.styleswitch.isChecked = true
                binding.styleswitch.visibility = View.VISIBLE

                // Setzt den AutoCompleteTextView auf '1' und beschränkt die Auswahl
                binding.numberListACT.setText("1")
                binding.numberListACT.setAdapter(
                    ArrayAdapter(
                        view.context,
                        android.R.layout.simple_list_item_1,
                        (1..1).toList()
                    )
                )
            } else {
                // Setzt den AutoCompleteTextView zurück auf Werte von 1 bis 10, wenn HQ deaktiviert ist
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
                maxlength = 1000  // Setzt die maximale Textlänge zurück

                // Verbirgt zusätzliche UI-Elemente, wenn HQ-Modus deaktiviert ist
                binding.styleswitch.isChecked = false
                binding.styleswitch.visibility = View.GONE
            }
        }


// Klick-Listener für den Generieren-Button
        binding.generateBtn.setOnClickListener {
            view.context.hideKeyBoard(it)  // Verbirgt die Tastatur
            binding.generateBtn.visibility = View.GONE  // Versteckt den Generieren-Button

            if (binding.edPrompt.text.toString().trim().isNotEmpty()) {
                // Überprüft die Länge des Eingabetextes
                if (binding.edPrompt.text.toString().trim().length < maxlength) {
                    // Loggt die eingegebenen Daten
                    Log.d("Prompt", binding.edPrompt.text.toString().trim())
                    Log.d("numberListACT", binding.numberListACT.text.toString().trim())

                    // Ermittelt den ausgewählten Radio-Button für die Bildgröße
                    val selectedSizeRB = view.findViewById<RadioButton>(binding.imageSizeRG.checkedRadioButtonId)
                    Log.d("selectedSizeRB", selectedSizeRB.text.toString().trim())

                    // Bestimmt die Werte basierend auf der Auswahl der Radio-Buttons und Switches
                    val qualityValue = if (binding.radioBtn3.isChecked && binding.hqswitch.isActivated) "standard" else "hd"
                    val styleValue = if (binding.radioBtn3.isChecked && binding.hqswitch.isChecked && binding.styleswitch.isChecked) "vivid" else "natural"
                    val modelValue = if (binding.hqswitch.isChecked) "dall-e-3" else "dall-e-2"

                    // Sendet die Anfrage zum Erstellen eines Bildes
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

                    // Leert das Eingabefeld nach der Anfrage
                    binding.edPrompt.text = null
                } else {
                    view.context.longToastShow("The maximum length is 1000 characters.")
                }
            } else {
                view.context.longToastShow("Prompt is required")
            }
        }

// Initialisierung und Event-Handler für Bildansicht-Dialog
        val loadImagein = viewImageDialog.findViewById<ImageView>(R.id.loadImage3)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)

// Schließt den Dialog bei Klick auf den Abbrechen-Button
        cancelBtn.setOnClickListener {
            viewImageDialog.dismiss()
        }

// ImageAdapter mit Click-Listener, der beim Klick ein Bild im Dialog zeigt
        val imageAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImagein)
                .load(data.url)
                .placeholder(R.drawable.ic_placeholder)
                .into(loadImagein)

            // Download-Button im Dialog zum Herunterladen des Bildes
            downloadBtn.setOnClickListener {
                if (it.context.checkMultiplePermission()) {
                    it.context.download(data.url)
                } else {
                    appSettingOpen(it.context)
                }
            }
        }

// Setzt den Adapter für den RecyclerView
        binding.imageRv.adapter = imageAdapter

// Listener für den Download-All-Button
        binding.downloadAllBtn.setOnClickListener {
            if (it.context.checkMultiplePermission()) {
                // Startet den Download für alle Bilder in der Liste
                imageAdapter.currentList.map { list ->
                    it.context.download(list.url)
                }
            } else {
                appSettingOpen(it.context)
            }
        }

        // Asynchrone Verarbeitung des Zustands der Bildladevorgänge
        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect {
                when (it.status) {
                    Status.LOADING -> {
                        // Zeigt einen Ladebalken im UI-Thread an, wenn die Bilder geladen werden
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.VISIBLE
                        }
                    }
                    Status.SUCCESS -> {
                        // Verbirgt den Ladebalken und aktualisiert die Bildliste im UI-Thread
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.GONE
                            imageAdapter.submitList(it.data?.data)

                            // Zeigt oder verbirgt den "Download All"-Button basierend auf der Liste
                            if (imageAdapter.currentList.isNotEmpty()) {
                                binding.downloadAllBtn.visibility = View.VISIBLE
                            } else {
                                binding.downloadAllBtn.visibility = View.GONE
                            }
                        }
                    }
                    Status.ERROR -> {
                        // Verbirgt den Ladebalken und zeigt eine Fehlermeldung an, wenn ein Fehler auftritt
                        withContext(Dispatchers.Main) {
                            binding.loadingPB.visibility = View.GONE
                            it.message?.let { message -> view.context.longToastShow(message) }
                        }
                    }
                }
            }
        }
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
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
        request.setTitle(fileName)  // Setzt den Titel der Download-Benachrichtigung
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Image/$fileName")
        downloadManager.enqueue(request)  // Startet den Download
    }

}


