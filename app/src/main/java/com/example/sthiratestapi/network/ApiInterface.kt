package com.example.sthiratestapi.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    @GET
    suspend fun getJadwal(@Url url: String?
    ): Response<com.example.sthiratestapi.model.Response>

}