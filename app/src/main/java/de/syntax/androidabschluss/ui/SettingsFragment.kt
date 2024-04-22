package de.syntax.androidabschluss.ui


import android.content.res.ColorStateList
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.utils.getAuoraColor
import de.syntax.androidabschluss.utils.getEvilColor
import de.syntax.androidabschluss.utils.getMatrixColor
import de.syntax.androidabschluss.utils.getRoseColor
import de.syntax.androidabschluss.utils.getSnowColor
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.PlayerViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel



class SettingsFragment : Fragment() {

    private var player: ExoPlayer? = null

    private lateinit var binding: FragmentSettingsBinding
    private val viewmodel: FirebaseViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    //Radioswitch...
    private val playerViewModel : PlayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamUrl = "https://sunsl.streamabc.net/sunsl-techno-mp3-192-4912904?sABC=6614015p%230%2306sr033o32rq9r1o2nn6qq9o3297no19%23fgernz.fhafuvar-yvir.qr&aw_0_1st.playerid=stream.sunshine-live.de&amsparams=playerid:stream.sunshine-live.de;skey:1712587100"

        //Speichert den aktuellen Switch zustand im viewmodel


        binding.weatherBtn.setOnClickListener{
            findNavController().navigate(R.id.weatherFragment)
        }
       playerViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
           binding.techno.isChecked = isPlaying

           if (isPlaying) {
               context?.let {  playerViewModel.initializePlayer(it, streamUrl) }
           }
       }

        binding.techno.setOnCheckedChangeListener { _, isChecked ->
            playerViewModel.playOrPause((isChecked))

            if (isChecked){
                context?.let {  playerViewModel.initializePlayer(it, streamUrl) }
            }
        }


        binding.toolbarLayout2.titletexttool.setText("Settings")
        binding.toolbarLayout2.backbutton.visibility = View.GONE

        sharedViewModel.strokecolor.observe(viewLifecycleOwner) { colorInt ->
            val colorStateList = ColorStateList.valueOf(colorInt)
            binding.cardviewtr.setStrokeColor(colorStateList)

        }


        binding.stylegroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
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
//hey also wenn man in der app aufs settingfragment geht kann man in der radiogroup auswählen welche farbe die strokes in der app haben dies alles funktioniert auch wenn man durch die app navigiert aber wenn man die app neu startet muss man die farbe auf dem settingsfragment wieder ändern ich möchte das der zustand beim verlassen der app bestehen bleib und beim erneuten öffnen der app die farbe bei den fargmenten automatisch den letzten zustand angepasst wird

    override fun onPause() {
        super.onPause()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        editor.putInt("selected_color", binding.stylegroup.checkedRadioButtonId)
        editor.apply()
    }
}

