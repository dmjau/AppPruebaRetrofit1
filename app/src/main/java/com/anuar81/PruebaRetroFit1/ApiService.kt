package com.anuar81.PruebaRetroFit1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getDogByBreds(@Url url:String): Call<DogsResponse>
}