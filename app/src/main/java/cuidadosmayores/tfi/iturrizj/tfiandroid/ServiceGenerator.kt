package cuidadosmayores.tfi.iturrizj.tfiandroid

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

const val BASE_URL: String = "http://172.16.66.72:8080/"
const val READ_TIMEOUT: Long = 40

class ServiceGenerator {

    companion object {
        private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        private val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())

        fun <S> createService(serviceClass: Class<S>): S? {
            var okHttpClientBuilder = OkHttpClient.Builder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor)

            return retrofitBuilder.client(okHttpClientBuilder.build()).build().create(serviceClass)
        }

    }

    private class HeaderInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                    .addHeader("idRequerimiento", UUID.randomUUID().toString())
                    .build()
            return chain.proceed(newRequest)
        }

    }

}