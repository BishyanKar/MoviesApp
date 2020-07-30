package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.myapplication.api.MovieInterface
import com.example.myapplication.api.POST_PER_PAGE
import com.example.myapplication.model.Movie
import com.example.myapplication.repo.MovieDataSource
import com.example.myapplication.repo.MovieDataSourceFactory
import com.example.myapplication.repo.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PagedListRepo(private val apiService : MovieInterface) {

    lateinit var pagedList : LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory : MovieDataSourceFactory

    fun fetchMoviesPagedList(compositeDisposable : CompositeDisposable) : LiveData<PagedList<Movie>>{
        movieDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        pagedList = LivePagedListBuilder(movieDataSourceFactory,config).build()

        return pagedList
    }
    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap(movieDataSourceFactory.movieLiveDataSource,MovieDataSource::networkState)
    }
}