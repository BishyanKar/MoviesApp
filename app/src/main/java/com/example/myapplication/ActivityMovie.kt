package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.api.POSTER_BASE_URL
import com.example.myapplication.databinding.ActivityMovieBinding
import com.example.myapplication.model.MovieDetails
import com.example.myapplication.repo.NetworkState
import com.example.myapplication.viewmodel.MovieViewModel

class ActivityMovie : AppCompatActivity() {
    private lateinit var viewModel : MovieViewModel
    private var movieId : Int = 1

    lateinit var binding: ActivityMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getIntExtra("id",1)

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        viewModel.movieDetails(movieId).observe(this, Observer {movieDetail->
            bindUI(movieDetail)
        })

        viewModel.networkState().observe(this, Observer {
            //actions pertaining to network state
            when(it){
                NetworkState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                NetworkState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext,"Network Error Occurred!",Toast.LENGTH_SHORT).show()
                }
                NetworkState.LOADED -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun bindUI(movie : MovieDetails){
        binding.movieTitle.text = movie.title
        binding.movieSubtitle.text = movie.tagline

        Glide.with(this)
            .load(POSTER_BASE_URL + movie.posterPath)
            .into(binding.movieImg)
    }
}