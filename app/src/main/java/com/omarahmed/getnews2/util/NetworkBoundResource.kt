package com.omarahmed.getnews2.util

import com.omarahmed.getnews2.data.api.NewsResponse
import com.omarahmed.getnews2.data.room.NewsEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },

    ) = flow {

    val data = query().first()

    val flow = when {
        (shouldFetch(data)) -> {
            query().map { (Resource.Loading(it)) }
            try {
                saveFetchResult(fetch())
                query().map { (Resource.Success(it)) }
            } catch (throwable: Throwable) {
                query().map { (Resource.Error(throwable, it)) }
            }
        }
        else -> query().map { (Resource.Success(it)) }
    }
    emitAll(flow)
}

inline fun <RequestType> networkBoundResourceApiOnly(
    crossinline fetch: suspend () -> Response<RequestType>
) = flow {

    emit(Resource.Loading(null))

    try {
        emit(Resource.Success(fetch().body()))
    }catch (throwable: Throwable){
        emit(Resource.Error(throwable,null))
    }
}