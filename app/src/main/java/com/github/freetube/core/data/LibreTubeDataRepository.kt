package com.github.freetube.core.data

import com.github.freetube.core.model.LibreTubeData
import kotlinx.coroutines.flow.Flow

interface LibreTubeDataRepository {
    val data: Flow<LibreTubeData>
}
