package com.ineedyourcode.groovymovie.view.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.utils.favoriteMap
import com.squareup.picasso.Picasso

class MoviesListAdapter :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    private lateinit var moviesList: List<Movie>
    private lateinit var mListener: OnItemClickListener
    private val mainPosterPath = "https://image.tmdb.org/t/p/"
    private val posterSize = "w342/"

    fun setAdapterData(moviesMap: Map<Int, Movie>) {
        notifyDataSetChanged()
        moviesList = moviesMap.values.toList()
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int, moviesList: List<Movie>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MoviesListViewHolder(itemView, mListener, moviesList)
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = moviesList[position].title
            movieRating.text = moviesList[position].rating

            isFavorite.setOnClickListener(View.OnClickListener {
                if (isFavorite.isChecked) {
                    isFavorite.showSnackWithoutAction("${movieTitle.text} добавлен в ИЗБРАННЫЕ")
                    favoriteMap[moviesList[position].id] = true
                } else {
                    isFavorite.showSnackWithoutAction("${movieTitle.text} удален из ИЗБРАННЫХ")
                    favoriteMap[moviesList[position].id] = false
                }
            })

            Picasso.get()
                .load("${mainPosterPath}${posterSize}${moviesList[position].posterPath}")
                .into(moviePoster)
        }
    }

    override fun getItemCount() = moviesList.size

    class MoviesListViewHolder(
        itemView: View,
        listener: OnItemClickListener,
        moviesList: List<Movie>
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClickListener(
                    absoluteAdapterPosition,
                    moviesList
                )
            }
        }

        val movieTitle: TextView = itemView.findViewById(R.id.txt_movie_title)
        val movieRating: TextView = itemView.findViewById(R.id.txt_movie_rating)
        val moviePoster: ImageView = itemView.findViewById(R.id.draw_movie_poster)
        val isFavorite: CheckBox = itemView.findViewById(R.id.checkbox_favorite)
    }
}