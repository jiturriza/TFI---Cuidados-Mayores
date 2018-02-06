package cuidadosmayores.tfi.iturrizj.tfiandroid

import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Idioma
import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Trayectoria
import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CuidadosMayoresServices {

    //region Cuenta
    @GET("api/cuenta/getIdiomas")
    fun getIdiomas(): Call<List<Idioma>>

    @POST("api/cuenta/login")
    fun login(@Body UID: String): Call<LoginResponse>

    @POST("api/cuenta/register")
    fun register(@Body request: Usuario): Call<Any>
    //endregion

    //region Location
    @GET("api/localizacion/trayectorias")
    fun getTrayectorias(): Call<List<Trayectoria>>

    //endregion

}