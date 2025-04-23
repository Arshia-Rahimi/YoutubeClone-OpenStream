package com.github.openstream.core.media3

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import com.github.openstream.core.datastore.proto.playerconfig.PlayerConfigDataStore
import com.github.openstream.core.datastore.proto.playerconfig.PlayerConfigDataStoreModel
import com.github.openstream.core.shared.exceptions.PlayerNotInitializedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform

@OptIn(UnstableApi::class)
class OpenStreamMediaPlayer(
    private val context: Context,
    configDataStore: PlayerConfigDataStore,
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val config = configDataStore.data
        .stateIn(scope, SharingStarted.Eagerly, PlayerConfigDataStoreModel())

    private var _player: ExoPlayer? = null
    val player: ExoPlayer
        get() = _player ?: throw PlayerNotInitializedException()

    // returns null when uninitialized
    private var _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    val playerPosition = playerState.transform { state ->
        if (state.playingStatus == PlayingStatus.PLAYING) {
            while (true) {
                emit(_player?.currentPosition?.div(1000) ?: 0L)
                delay(1000L)
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val status = if (isPlaying) PlayingStatus.PLAYING
            else if (player.playbackState == Player.STATE_BUFFERING) PlayingStatus.BUFFERING
            else PlayingStatus.PAUSED
            _playerState.getAndUpdate { it.copy(playingStatus = status) }
        }

        override fun onPlayerError(error: PlaybackException) {
            val cause = error.cause
            // todo catch httpError
            _playerState.getAndUpdate { it.copy(playerError = error.localizedMessage) }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            _playerState.getAndUpdate { it.copy(repeatMode = repeatMode) }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _playerState.getAndUpdate { it.copy(shuffleModeEnabled = shuffleModeEnabled) }
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            _playerState.getAndUpdate { it.copy(playbackSpeed = playbackParameters.speed) }
        }
    }

    fun init() {
        val seekIncrement = config.value.seekIncrement
        _player = ExoPlayer.Builder(context)
            .setSeekParameters(SeekParameters(seekIncrement, seekIncrement))
            .build()
        player.addListener(playerListener)
    }

    fun release() {
        _player?.removeListener(playerListener)
        _player?.release()
        _playerState.value = PlayerState()
        _player = null
    }

    fun prepareSingleVideo(video: MediaItem) {
        player.pause()
        player.clearMediaItems()
        player.setMediaItem(video)
        player.prepare()
    }

    fun resume() = player.play()

    fun pause() = player.pause()

    fun seekTo(ms: Long) = player.seekTo(ms)

    fun seekForward() = player.seekForward()

    fun seekBackward() = player.seekBack()

    fun next() {
        if (player.hasNextMediaItem()) player.seekToNextMediaItem()
    }

    fun previous() {
        if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem()
    }

    fun setRepeatMode(mode: Int) {
        player.repeatMode = mode
    }

    fun setShuffleMode(isShuffleEnabled: Boolean) {
        player.shuffleModeEnabled = isShuffleEnabled
    }

    fun setPlaybackSpeed(speed: Float) {
        player.playbackParameters.speed = speed
    }
}
