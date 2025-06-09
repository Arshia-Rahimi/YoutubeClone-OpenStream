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
import com.github.openstream.core.data.PlayerConfigRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class OpenStreamMediaPlayer(
    context: Context,
    private val playerConfigRepo: PlayerConfigRepository,
) {
    private var _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()
    
    val playerPosition = playerState.transform { state ->
        if (state.playingStatus == PlayingStatus.PLAYING) {
            while (true) {
                emit(player.currentPosition / 1000)
                delay(1000L)
            }
        } else {
            emit(player.currentPosition / 1000)
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
            // todo catch and show playerError
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
    
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    val player: ExoPlayer = ExoPlayer.Builder(context).build()
        .apply {
            addListener(playerListener)
            playerConfigRepo.playerConfig.onEach {
                player.apply {
                    setSeekParameters(SeekParameters(it.seekIncrement, it.seekIncrement))
                    repeatMode = it.playerRepeatMode.ordinal
                    shuffleModeEnabled = it.isShuffleEnabled
                    setPlaybackSpeed(it.playbackSpeed)
                }
            }.launchIn(scope)
        }
    
    fun prepareSingleVideo(video: MediaItem) {
        player.pause()
        player.clearMediaItems()
        player.setMediaItem(video)
        player.prepare()
    }
    
    fun clear() {
        player.pause()
        player.clearMediaItems()
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
    
    fun setRepeatMode(repeatMode: PlayerRepeatMode) {
        scope.launch {
            playerConfigRepo.setRepeatMode(repeatMode)
        }
    }
    
    fun toggleShuffleMode() {
        scope.launch {
            playerConfigRepo.setShuffleMode(!player.shuffleModeEnabled)
        }
    }
    
    fun setPlaybackSpeed(speed: Float) {
        scope.launch {
            player.setPlaybackSpeed(speed)
        }
    }
    
}
