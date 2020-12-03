package tech.ctawave.exoApp.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.ctawave.exoApp.util.AppExecutors
import tech.ctawave.exoApp.data.remote.ApiEmptyResponse
import tech.ctawave.exoApp.data.remote.ApiErrorResponse
import tech.ctawave.exoApp.data.remote.ApiResponse
import tech.ctawave.exoApp.data.remote.ApiSuccessResponse
import tech.ctawave.exoApp.util.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * @param <ResultType>
 * @param <RequestType>
 */
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MutableStateFlow<Resource<ResultType>>(Resource.loading(null))

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchFromDatabaseOrNetwork()
        }
    }

    private suspend fun fetchFromDatabaseOrNetwork() {
        val databaseSource = loadFromDatabase()

        databaseSource.collect { dbSource ->
            if (shouldFetch(dbSource)) {
                fetchFromNetwork(dbSource)
            } else {
                setValue(Resource.success(dbSource))
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private suspend fun fetchFromNetwork(databaseSource: ResultType) {
        val apiResponse = createCall()

        // we re-attach databaseSource as a new source, it will dispatch its latest value quickly
        setValue(Resource.loading(databaseSource))

        apiResponse.collect { response ->
            when (response) {
                is ApiSuccessResponse -> {
                    saveCallResult(processResponse(response))
                    loadFromDatabase().collect {
                        setValue(Resource.success(it))
                    }
                }
                is ApiEmptyResponse -> {
                    loadFromDatabase().collect {
                        setValue(Resource.success(it))
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, databaseSource))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asFlow() = result as Flow<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDatabase(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

}
