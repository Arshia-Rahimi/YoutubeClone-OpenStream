package com.github.freetube.core.media3

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import com.github.freetube.core.datastore.proto.playerconfig.PlayerConfigDataStore
import com.github.freetube.core.datastore.proto.playerconfig.PlayerConfigDataStoreModel
import com.github.freetube.core.extractor.video.VideoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@OptIn(UnstableApi::class)
class LibreTubeMediaPlayer(
    private val context: Context,
    configDataStore: PlayerConfigDataStore,
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val config = configDataStore.data
        .stateIn(scope, SharingStarted.WhileSubscribed(5000L), PlayerConfigDataStoreModel())

    private var _player: ExoPlayer? = null
    val player: ExoPlayer
        get() = _player ?: throw PlayerNotInitializedException()

    // returns null when uninitialized
    private var _playerState = MutableStateFlow<PlayerState?>(null)
    val playerState = _playerState.asStateFlow()

    // null when not in playlistMode
    private var currentPlaylist: List<VideoData>? = emptyList<VideoData>()

    private var _currentVideo = MutableStateFlow<VideoData?>(null)
    val currentVideo = _currentVideo.asStateFlow()

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val playerPosition = playerState.flatMapLatest { state ->
        if (state == null) flow { emit(0L) }
        flow {
            while (true) {
                if (playerState.value?.playingStatus == PlayingStatus.PLAYING) {
                    emit(player.currentPosition)
                }
                delay(1000L)
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            val status = if (isPlaying) PlayingStatus.PLAYING
            else if (player.playbackState == Player.STATE_BUFFERING) PlayingStatus.BUFFERING
            else PlayingStatus.PAUSED
            update(_playerState.value?.copy(playingStatus = status))
        }

        override fun onPlayerError(error: PlaybackException) {
            val cause = error.cause
            // todo catch httpError
            update(_playerState.value?.copy(playerError = error.localizedMessage))
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            currentPlaylist?.let {
                _currentVideo.value = it.first { video -> video.mediaItem == mediaItem }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            update(_playerState.value?.copy(repeatMode = repeatMode))
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            update(_playerState.value?.copy(shuffleModeEnabled = shuffleModeEnabled))
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            update(_playerState.value?.copy(playbackSpeed = playbackParameters.speed))
        }

        private fun update(newState: PlayerState?) {
            _playerState.value = newState
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
        player.removeListener(playerListener)
        player.release()
        _playerState.value = null
        _player = null
        _currentVideo.value = null
        currentPlaylist = null
    }

    fun prepareSingleVideo(video: VideoData) {
        player.setMediaItem(video.mediaItem)
        player.prepare()
        _currentVideo.value = video
        currentPlaylist = null
    }

    fun prepareFromPlaylist(playlist: List<VideoData>) {
        val mediaItems = playlist.map { it.mediaItem }
        player.setMediaItems(mediaItems, true)
        player.prepare()
        currentPlaylist = playlist
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
