package com.ntp.notengoplan
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Query


interface ApiService {

    @GET("?action=usuarios&{id}") // Ejemplo de endpoint
    fun getUser(@Path("id") userId: Int): Call<usuarios> // Define el tipo de retorno


    @GET("?action=usuarios") // Ejemplo de endpoint
    fun getUserS():  Call<List<usuarios>> // Define el tipo de retorno

    @DELETE("?action=usuarios") // Define la ruta sin la llave en la URL
    fun delUser(@Query("id") userId: Int): Call<Void> // Usa @Query para parámetros de consulta

}