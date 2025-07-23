package com.github.openstream.core.common.util

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
    data class Error(val error: Throwable? = null) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}

fun <T> Flow<T>.asResult(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    caller: String? = null,
    message: String? = null,
): Flow<Resource<T>> =
    map<T, Resource<T>> { Resource.Success(it) }
        .onStart {
            getLogger().i("Resource: $caller", "$message")
            emit(Resource.Loading)
        }
        .catch {
            getLogger().e("Resource: $caller", "resource error", it)
            coroutineContext.ensureActive()
            emit(Resource.Error(it))
        }.flowOn(dispatcher)

data object Success
