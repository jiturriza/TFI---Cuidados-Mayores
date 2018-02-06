package cuidadosmayores.tfi.iturrizj.tfiandroid.BLL

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 open class Callback<T> : Callback<T> {

    override fun onFailure(call: Call<T>?, t: Throwable?) {

    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response!!.isSuccessful) onSuccess()

    }

    open fun onSuccess() {

    }
}