package com.ntp.notengoplan
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("?action=usuarios&{id}") // Ejemplo de endpoint
    fun getUser(@Path("id") userId: Int): Call<usuarios> // Define el tipo de retorno


    @GET("?action=usuarios") // Ejemplo de endpoint
    fun getUserS():  Call<List<usuarios>> // Define el tipo de retorno




}