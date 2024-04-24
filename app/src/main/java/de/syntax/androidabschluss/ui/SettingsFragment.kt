package de.syntax.androidabschluss.ui


import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.utils.getAuoraColor
import de.syntax.androidabschluss.utils.getEvilColor
import de.syntax.androidabschluss.utils.getMatrixColor
import de.syntax.androidabschluss.utils.getRoseColor
import de.syntax.androidabschluss.utils.getSnowColor
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.PlayerViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel



class SettingsFragment : Fragment() {


    private lateinit var binding: FragmentSettingsBinding
    private val viewmodel: FirebaseViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    //Radioswitch...
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.weatherBtn.setOnClickListener {
            findNavController().navigate(R.id.weatherFragment)
        }

        // Beobachtet Änderungen der E-Mail-Adresse des Benutzers
        viewmodel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.theMail.text = email ?: "Keine E-Mail verfügbar"
        }

        //Setzt einen Click-Listener für den Button zur Passwortänderung
        binding.changePassword.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_change_password, null)
            val currentPasswordInput = view.findViewById<EditText>(R.id.currentPassword)
            val newPasswordInput = view.findViewById<EditText>(R.id.newPassword)

            // Erstellt und zeigt einen Dialog zum Ändern des Passworts
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Passwort ändern")
                .setView(view)
                .setPositiveButton("Speichern") { dialog, _ ->
                    val currentPassword = currentPasswordInput.text.toString()
                    val newPassword = newPasswordInput.text.toString()

                    // Überprüft die Eingaben und startet den Passwortänderungsprozess
                    if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                        context?.longToastShow("Bitte füllen Sie alle Felder aus")
                    } else {
                        val user = FirebaseAuth.getInstance().currentUser
                        val credential =
                            EmailAuthProvider.getCredential(user!!.email!!, currentPassword)

                        // Ändern Passwort den Benutzer mit den aktuellen Anmeldeinformationen
                        user.reauthenticate(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Aktualisiert das Passwort, wenn die Reauthentifizierung erfolgreich war
                                user.updatePassword(newPassword)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            context?.longToastShow("Passwort erfolgreich geändert")
                                        } else {
                                            context?.longToastShow("Fehler beim Ändern des Passworts: ${updateTask.exception?.message}")
                                        }
                                    }
                            } else {
                                context?.longToastShow("Fehler bei der Authentifizierung: ${task.exception?.message}")
                            }
                        }
                    }
                }
                .setNegativeButton("Abbrechen", null)
                .create()
            dialog.show()
        }

        // Setzt einen Click-Listener für den Button zum Zurücksetzen des Passworts
        binding.resetPasswordBtn.setOnClickListener {
            viewmodel.userEmail.value?.let { email ->
                if (email.isNotEmpty()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                context?.longToastShow("Ein Link zum Zurücksetzen Ihres Passworts wurde gesendet")
                            } else {
                                context?.longToastShow("Fehler beim Senden des Reset-Links: ${task.exception?.message}")
                            }
                        }
                } else {
                    context?.longToastShow("Keine registrierte E-Mail-Adresse gefunden")
                }
            }
        }

//------------------Radio
        val streamUrl =
            "https://sunsl.streamabc.net/sunsl-techno-mp3-192-4912904?sABC=6614015p%230%2306sr033o32rq9r1o2nn6qq9o3297no19%23fgernz.fhafuvar-yvir.qr&aw_0_1st.playerid=stream.sunshine-live.de&amsparams=playerid:stream.sunshine-live.de;skey:1712587100"

        playerViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            binding.techno.isChecked = isPlaying
            if (isPlaying) {
                context?.let { playerViewModel.initializePlayer(it, streamUrl) }
            }
        }

        // Steuert die Wiedergabe basierend auf der Schalterposition
        binding.techno.setOnCheckedChangeListener { _, isChecked ->
            playerViewModel.playOrPause(isChecked)
            if (isChecked) {
                context?.let { playerViewModel.initializePlayer(it, streamUrl) }
            }
        }


        // Setzt den Titel der Toolbar und macht den Zurück-Button unsichtbar
        binding.toolbarLayout2.titletexttool.setText("Settings")
        binding.toolbarLayout2.backbutton.visibility = View.GONE

        //Beobachtet Änderungen der Rahmenfarbe, die durch das ViewModel bereitgestellt wird
        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { colorInt ->
            val colorStateList = ColorStateList.valueOf(colorInt)
            binding.cardviewtr.setStrokeColor(colorStateList)
        }

        binding.stylegroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbblure -> sharedViewModel.updatecolor(getAuoraColor(requireContext()))
                R.id.rbgreen -> sharedViewModel.updatecolor(getMatrixColor(requireContext()))
                R.id.rbwhite -> sharedViewModel.updatecolor(getSnowColor(requireContext()))
                R.id.rbrose -> sharedViewModel.updatecolor(getRoseColor(requireContext()))
                R.id.rbred -> sharedViewModel.updatecolor(getEvilColor(requireContext()))
            }
        }

        binding.logout.setOnClickListener {
            viewmodel.logout()
            findNavController().navigate(R.id.homeFragment)
        }
    }

        // Speichert die aktuell ausgewählte Radio-Button-ID beim Pausieren der Ansicht
        override fun onPause() {
            super.onPause()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val editor = sharedPreferences.edit()
            editor.putInt("selected_color", binding.stylegroup.checkedRadioButtonId)
            editor.apply()
        }
}
