package com.example.scannerapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("products")

    fun getApi(

        @Query("barcode")barcode:String,
        @Query("formatted")formatted:String,
        @Query("key")apiKey:String


    ):Call<ApiResponse>


}