package com.example.myapplication.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.myapplication.api.MovieInterface
import com.example.myapplication.model.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable


class MovieDataSourceFactory(private val apiService:MovieInterface ,private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Movie>() {

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {

        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}