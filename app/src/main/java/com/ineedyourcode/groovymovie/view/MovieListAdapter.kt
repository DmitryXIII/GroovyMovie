package com.ineedyourcode.groovymovie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.Movie

class MovieListAdapter :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    private var moviesList: List<Movie> = listOf()
    private lateinit var mListener: OnItemClickListener

    fun setAdapterData(moviesList: List<Movie>) {
        notifyDataSetChanged()
        this.moviesList = moviesList
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieTitle.text = moviesList[position].title
        holder.movieRating.text = moviesList[position].rating
        holder.movieReleaseDate.text = moviesList[position].releaseDate
        holder.movieGenre.text = moviesList[position].genre
        holder.moviePoster.setImageResource(moviesList[position].poster)
    }

    override fun getItemCount() = moviesList.size

    class MovieViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { listener.onItemClickListener(absoluteAdapterPosition) }
        }

        val movieTitle: TextView = itemView.findViewById(R.id.txt_movie_title)
        val movieRating: TextView = itemView.findViewById(R.id.txt_movie_rating)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.txt_movie_release_date)
        val movieGenre: TextView = itemView.findViewById(R.id.txt_movie_genre)
        val moviePoster: ImageView = itemView.findViewById(R.id.draw_movie_poster)
    }
}
