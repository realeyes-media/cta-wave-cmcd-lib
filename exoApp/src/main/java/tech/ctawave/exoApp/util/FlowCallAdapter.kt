package tech.ctawave.exoApp.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import tech.ctawave.exoApp.data.remote.ApiResponse
import java.lang.reflect.Type

/**
 * A Retrofit adapter that converts the Call into a StateFlow of ApiResponse
 *
 * @param <R>
 */
class FlowCallAdapter<R>(private val responseType: Type): CallAdapter<R, Flow<ApiResponse<R>>> {
    @ExperimentalCoroutinesApi
    override fun adapt(call: Call<R>): Flow<ApiResponse<R>> {
        return flow {
            emit(
                suspendCancellableCoroutine { cont ->
                    call.enqueue(object: Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            cont.resume(ApiResponse.create(response), null)
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            cont.resume(ApiResponse.create<R>(t), null)
                        }
                    })
                    cont.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}
