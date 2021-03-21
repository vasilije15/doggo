package com.vasilije.doggo.retrofit

import com.vasilije.doggo.model.Request
import retrofit2.Call
import retrofit2.http.GET

interface DogsApi {

    @GET("breeds/image/random/50")
    fun getDogsPictures(): Call<Request>
}