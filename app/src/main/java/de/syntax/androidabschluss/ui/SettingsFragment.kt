package de.syntax.androidabschluss.ui


import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.utils.getAuoraColor
import de.syntax.androidabschluss.utils.getEvilColor
import de.syntax.androidabschluss.utils.getMatrixColor
import de.syntax.androidabschluss.utils.getRoseColor
import de.syntax.androidabschluss.utils.getSnowColor
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.SharedViewModel



class SettingsFragment : Fragment() {

    private var player: ExoPlayer? = null

    private lateinit var binding: FragmentSettingsBinding
    private val viewmodel: FirebaseViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.techno.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                initializePlayer()
            } else {
                player?.release()
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
    private fun initializePlayer() {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()

            val streamUrl = "https://sunsl.streamabc.net/sunsl-techno-mp3-192-4912904?sABC=6614015p%230%2306sr033o32rq9r1o2nn6qq9o3297no19%23fgernz.fhafuvar-yvir.qr&aw_0_1st.playerid=stream.sunshine-live.de&amsparams=playerid:stream.sunshine-live.de;skey:1712587100"
            val mediaSource = buildMediaSource(Uri.parse(streamUrl))

            player?.let { exoPlayer ->
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))
    }



}



