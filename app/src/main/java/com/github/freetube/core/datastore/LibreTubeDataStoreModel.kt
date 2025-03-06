package com.github.freetube.core.datastore

import com.github.freetube.core.model.AppTheme
import kotlinx.serialization.Serializable

@Serializable
data class LibreTubeDataStoreModel(
    val appTheme: AppTheme = AppTheme.System,
) 
