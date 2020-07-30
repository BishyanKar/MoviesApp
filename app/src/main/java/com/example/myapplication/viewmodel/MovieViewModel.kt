package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.myapplication.PagedListRepo
import com.example.myapplication.model.MovieDetails
import com.example.myapplication.api.MovieClient
import com.example.myapplication.model.Movie
import com.example.myapplication.repo.MovieDetRepo
import com.example.myapplication.repo.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MovieViewModel : ViewModel(){
    private val compositeDisposable = CompositeDisposable()
    private val apiService = MovieClient.getClient()
    private val movieDetRepo =  MovieDetRepo(apiService)
    private val pagedListRepo = PagedListRepo(apiService)

    fun movieDetails(movieId : Int) : LiveData<MovieDetails> = movieDetRepo.fetchMovieDetails(compositeDisposable,movieId)

    fun networkState() : LiveData<NetworkState> = movieDetRepo.getMovieDetNetworkState()

    fun moviePagedList() : LiveData<PagedList<Movie>> = pagedListRepo.fetchMoviesPagedList(compositeDisposable)
    fun listNetworkState() : LiveData<NetworkState> = pagedListRepo.getNetworkState()




    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}