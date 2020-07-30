package com.example.myapplication.api

import com.example.myapplication.model.MovieDetails
import com.example.myapplication.model.MovieResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id : Int) : Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page : Int) : Single<MovieResponse>

}