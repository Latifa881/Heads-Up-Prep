package com.example.headsupprep

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/celebrities/")
    fun getCelebrityDetails(): Call<List<Celebrity.CelebrityDetails>>

    @GET("/celebrities/{id}")
    fun getOneCelebrityDetails(@Path("id") id: Int): Call<Celebrity.CelebrityDetails>

    @Headers("Content-Type: application/json")
    @POST("/celebrities/")
    fun addCelebrityDetails(@Body details: Celebrity.CelebrityDetails): Call<List<Celebrity.CelebrityDetails>>

    @Headers("Content-Type: application/json")
    @PUT("/celebrities/{id}")
    fun updateCelebrityDetails(
        @Path("id") id: Int,
        @Body details: Celebrity.CelebrityDetails
    ): Call<Celebrity.CelebrityDetails>

    @Headers("Content-Type: application/json")
    @DELETE("/celebrities/{id}")
    fun deleteCelebrityDetails(@Path("id") id: Int): Call<Void>
}