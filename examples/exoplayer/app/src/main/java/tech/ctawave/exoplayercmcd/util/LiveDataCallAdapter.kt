package tech.ctawave.exoplayercmcd.util

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import tech.ctawave.exoplayercmcd.data.remote.ApiResponse
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse
 *
 * @param <R>
 */
class LiveDataCallAdapter<R>(private val responseType: Type): CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        return object: LiveData<ApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object: Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(ApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            postValue(ApiResponse.create(t))
                        }
                    })
                }
            }
        }
    }

    override fun responseType(): Type {
        return responseType
    }

}
