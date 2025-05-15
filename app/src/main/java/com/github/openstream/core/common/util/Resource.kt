package com.github.openstream.core.common.util

import android.util.Log
import com.github.openstream.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.coroutineContext

sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String? = null) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}

fun <T> Flow<T>.asResult(dispatcher: CoroutineDispatcher = Dispatchers.Default): Flow<Resource<T>> =
    map<T, Resource<T>> { Resource.Success(it) }
    .onStart { emit(Resource.Loading) }
    .catch {
        if (BuildConfig.DEBUG) Log.d("Resource", "${it.cause}: ${it.localizedMessage}")
        coroutineContext.ensureActive()
        emit(Resource.Error(it.message))
    }.flowOn(dispatcher)

data object Success
