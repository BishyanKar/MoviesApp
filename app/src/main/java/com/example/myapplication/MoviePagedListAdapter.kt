package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.api.POSTER_BASE_URL
import com.example.myapplication.model.Movie
import com.example.myapplication.repo.NetworkState
import kotlinx.android.synthetic.main.loading_item_layout.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MoviePagedListAdapter(public val context: Context) : PagedListAdapter<Movie,RecyclerView.ViewHolder>(DiffUtilCallback){

    val MOVIE_VIEW_TYPE = 1
    val LOADING_VIEW_TYPE = 2

    private var networkState : NetworkState? = null

    object DiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    fun setNetworkState(networkState: NetworkState?){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }
            else{
                notifyItemInserted(super.getItemCount())
            }
        }
        else if(hasExtraRow && previousState!=networkState)
        {
            notifyItemChanged(itemCount-1)
        }
    }

    private fun hasExtraRow() : Boolean {
        return networkState!=null && networkState!=NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1){
            LOADING_VIEW_TYPE
        }
        else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view : View

        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item,parent,false)
            return MovieItemViewHolder(view)
        }
        else {
            view = layoutInflater.inflate(R.layout.loading_item_layout,parent,false)
            return NetworkViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as NetworkViewHolder).bind(networkState,context)
        }
    }

    class MovieItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie? ,context: Context){
            itemView.list_item_title.text = movie?.title
            itemView.list_item_date.text = movie?.releaseDate
            val posterUrl = POSTER_BASE_URL + movie?.posterPath

            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.list_item_img)

            itemView.setOnClickListener{
                val intent = Intent(context,ActivityMovie::class.java)
                intent.putExtra("id",movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(networkState : NetworkState?,context: Context){
            if(networkState!=null && networkState == NetworkState.LOADING)
            {
                itemView.progress_bar.visibility = View.VISIBLE
                Log.d("ADAPTER", "bind: SHOW_PROGRESS")
            }
            else{
                itemView.progress_bar.visibility = View.GONE
                Log.d("ADAPTER", "bind: DON'T SHOW_PROGRESS")
            }

            if(networkState!=null && networkState == NetworkState.ERROR){
                Toast.makeText(context,networkState.msg,Toast.LENGTH_SHORT).show()
            }
            else if(networkState!=null && networkState == NetworkState.EOL){
                Toast.makeText(context,networkState.msg,Toast.LENGTH_SHORT).show()
            }
        }
    }

}