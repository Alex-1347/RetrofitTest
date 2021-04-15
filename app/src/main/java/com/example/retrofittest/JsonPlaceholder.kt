package com.example.retrofittest


import retrofit2.Call
import retrofit2.http.*


interface JsonPlaceholder {
    @GET("GetCities")
    fun GetCities(): Call<MutableList<OneItem>>
}