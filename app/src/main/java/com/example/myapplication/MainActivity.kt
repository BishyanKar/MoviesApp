package com.example.myapplication

import android.app.usage.NetworkStats
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Movie
import com.example.myapplication.repo.NetworkState
import com.example.myapplication.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MovieViewModel
    private lateinit var binding : ActivityMainBinding
    private  var moviesData = ArrayList<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        val moviePagedListAdapter = MoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,2)

        gridLayoutManager.spanSizeLookup = object  : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                if(moviePagedListAdapter.getItemViewType(position) == moviePagedListAdapter.MOVIE_VIEW_TYPE)
                    return 1
                else return 2
            }
        }

        binding.recyclerView.adapter = moviePagedListAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = gridLayoutManager

        viewModel.moviePagedList().observe(this, Observer {movies->
            moviePagedListAdapter.submitList(movies)
            moviesData.clear()
            moviesData.addAll(movies)
        })

        viewModel.listNetworkState().observe(this, Observer {state->

            if(moviesData.isEmpty() && NetworkState.LOADING == state){
                Log.d("MAIN", "onCreate: PROGRESS_VISIBLE")
                binding.progressBar.visibility = View.VISIBLE
            }
            else {
                binding.progressBar.visibility = View.GONE
                Log.d("MAIN", "onCreate: PROGRESS_GONE")
            }
            if(moviesData.isEmpty() && NetworkState.ERROR == state){
                Toast.makeText(this,"Error loading data",Toast.LENGTH_SHORT).show()
            }

            if(moviesData.isNotEmpty()){
                moviePagedListAdapter.setNetworkState(state)
            }
        })

    }
}