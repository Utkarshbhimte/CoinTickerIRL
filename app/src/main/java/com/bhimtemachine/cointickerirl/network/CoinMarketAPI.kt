package com.bhimtemachine.cointickerirl.network

import com.bhimtemachine.cointickerirl.model.CoinDataResponse

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by bapspatil
 */

interface CoinMarketAPI {

    @GET("ticker/?limit=10")
    fun coinData(): Call<ArrayList<CoinDataResponse>>

    companion object {

        val BASE_URL = "https://api.coinmarketcap.com/v1/"

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}
