package com.searchandroidlist.apicalling

import com.google.gson.JsonElement
import retrofit2.http.*

interface RetrofitApi {

    @GET("all")
    suspend fun getCountryList(): JsonElement

    @GET("getSurvey/{id}")
    suspend fun getCountry(
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): JsonElement
}