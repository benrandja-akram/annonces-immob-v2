package api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


typealias ApiConsumer<T> = (T?) -> Unit

class ApiController<T>(val consume: ApiConsumer<T>): Callback<T>{
    override fun onFailure(call: Call<T>, t: Throwable) {
        throw t
    }
    override fun onResponse(call: Call<T>, response: Response<T>) {
        consume(response.body())
    }
}