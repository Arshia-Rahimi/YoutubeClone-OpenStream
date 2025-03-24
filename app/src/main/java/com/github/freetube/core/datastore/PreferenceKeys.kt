package com.github.freetube.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")


//
//class PreferencesDataStore(
//    private val context: Context,
//) {
//    
//    fun PreferenceKeys.get() = context.dataStore.data.map { it[key] }
//    
//    suspend fun PreferenceKeys.set(v: Any) = context.dataStore.edit { it[key] = v }
//    
//    enum class PreferenceKeys(
//        val key: Preferences.Key<*>,
//    ) {
//        DefaultVideoResolution(stringPreferencesKey("default_video_resolution"))
//    }
//
//}
