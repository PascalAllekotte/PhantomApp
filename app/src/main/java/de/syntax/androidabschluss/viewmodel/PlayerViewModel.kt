package de.syntax.androidabschluss.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class PlayerViewModel : ViewModel() {
    private var player : ExoPlayer? = null
    private val _isplaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isplaying


    fun initializePlayer(context: Context, streamUrl: String){
        if (player == null) {
            player = SimpleExoPlayer.Builder(context).build().apply {
                val mediaSource = buildMediaSource(Uri.parse(streamUrl), context)
                setMediaSource(mediaSource)
                prepare()
            }
        }
    }

    private fun buildMediaSource(uri: Uri, context: Context): MediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri))
    }

    fun playOrPause(shouldPlay: Boolean){
        _isplaying.value = shouldPlay
        player?.playWhenReady = shouldPlay
    }

    override fun onCleared(){
        super.onCleared()
        player?.release()
        player = null
    }



}