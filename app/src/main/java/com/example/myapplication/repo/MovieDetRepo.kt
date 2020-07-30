package com.example.myapplication.repo

import androidx.lifecycle.LiveData
import com.example.myapplication.model.MovieDetails
import com.example.myapplication.api.MovieInterface
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MovieDetRepo (private val apiService : MovieInterface){
    lateinit var movieDataSource : MovieDetailsDataSource

    fun fetchMovieDetails(compositeDisposable : CompositeDisposable, movieId : Int) : LiveData<MovieDetails>{
        movieDataSource = MovieDetailsDataSource(apiService,compositeDisposable)
        movieDataSource.fetchMovieDetails(movieId)

        return movieDataSource.downloadMovieDetailsResponse
    }

    fun getMovieDetNetworkState() : LiveData<NetworkState>{
        return movieDataSource.networkState
    }
}