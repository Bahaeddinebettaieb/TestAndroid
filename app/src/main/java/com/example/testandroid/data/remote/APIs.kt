package com.example.testandroid.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIs {

    @GET(ApiEndPoints.LIST_CITIES)
    suspend fun getListCities(): WebServiceResponse

    @GET(ApiEndPoints.LIST_ADS)
    suspend fun getListAds(@Path("lang") lang: String): WebServiceResponse

}