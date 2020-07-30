package com.example.myapplication.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit

const val API_KEY = "2d872e827cac4ff80e5d59d210cbfbee"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

object MovieClient {

    fun getClient() : MovieInterface {


      val okHttpClient =  OkHttpClient.Builder()
            .addInterceptor(
                Interceptor{chain->
                    val url = chain.request()
                        .url
                        .newBuilder()
                        .addQueryParameter("api_key",API_KEY)
                        .build()
                    val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()

                    return@Interceptor chain.proceed(request)
            }).connectTimeout(60,TimeUnit.SECONDS).build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieInterface::class.java)
    }
}