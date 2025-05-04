package com.github.openstream.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.openstream.core.database.entities.PlaylistVideoCrossRef
import com.github.openstream.core.database.entities.relationships.PlaylistWithVideos
import com.github.openstream.core.database.entities.relationships.VideoWithPlaylists

@Dao
interface M2mDao {
    @Insert
    suspend fun addToPlaylist(vararg playlistVideoCrossRef: PlaylistVideoCrossRef)

    @Transaction
    @Query("SELECT * FROM videos WHERE videoId = :id")
    suspend fun getVideoWithPlaylists(id: Long): VideoWithPlaylists?

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    suspend fun getPlaylistWithVideos(id: Long): PlaylistWithVideos?
}
