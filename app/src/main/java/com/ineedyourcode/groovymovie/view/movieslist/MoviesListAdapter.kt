package com.ineedyourcode.groovymovie.view.movieslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto
import com.ineedyourcode.groovymovie.utils.setBackgroundColorByRating
import com.squareup.picasso.Picasso

private const val MAIN_POSTER_PATH = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w342/"

class MoviesListAdapter :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    private lateinit var moviesList: List<TmdbMovieByIdDto>
    private lateinit var mListener: OnItemClickListener

    fun setAdapterData(moviesListFromFragment: List<TmdbMovieByIdDto>) {
        moviesList = moviesListFromFragment
        notifyItemRangeChanged(0, moviesList.size)
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int, moviesList: List<TmdbMovieByIdDto>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MoviesListViewHolder(itemView, mListener, moviesList)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = "\"${moviesList[position].title}\" (${
                moviesList[position].releaseDate.substring(0, 4)
            })"

            ratingBackground.setBackgroundColorByRating(moviesList[position].voteAverage)
            movieRating.text = moviesList[position].voteAverage.toString()

            Picasso.get()
                .load("${MAIN_POSTER_PATH}${POSTER_SIZE}${moviesList[position].posterPath}")
                .error(R.drawable.no_backdrop)
                .into(moviePoster)
        }
    }

    override fun getItemCount() = moviesList.size

    class MoviesListViewHolder(
        itemView: View,
        listener: OnItemClickListener,
        moviesList: List<TmdbMovieByIdDto>
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
        val ratingBackground: CardView = itemView.findViewById(R.id.rating_background)
    }
}