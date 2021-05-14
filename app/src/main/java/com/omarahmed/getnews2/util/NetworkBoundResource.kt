package com.omarahmed.getnews2.util

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
    crossinline onFetchSuccess: () -> Unit = {},
    crossinline onFetchFailed: (Throwable) -> Unit = {},

    ) = channelFlow{

    val data = query().first()

   if (shouldFetch(data)){
            //Loading..
            val loading = launch {
                query().collect { send(Resource.Loading(null)) }
            }

            //Get result
            try {
                saveFetchResult(fetch())
                onFetchSuccess()
                loading.cancel()
                query().collect { send(Resource.Success(it)) }
            }
            //Catch errors
            catch (throwable: Throwable) {
                onFetchFailed(throwable)
                loading.cancel()
                query().collect { send(Resource.Error(throwable, it)) }
            }
        }
        else  {query().collect { send(Resource.Success(it))}
    }
}

inline fun <RequestType> networkBoundResourceApiOnly(
    crossinline fetch: suspend () -> Response<RequestType>
) = flow {

    emit(Resource.Loading(null))

    try {
        emit(Resource.Success(fetch().body()))
    } catch (throwable: Throwable) {
        emit(Resource.Error(throwable, null))
    }
}