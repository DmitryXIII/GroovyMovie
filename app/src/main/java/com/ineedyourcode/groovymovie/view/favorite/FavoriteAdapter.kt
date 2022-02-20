package com.ineedyourcode.groovymovie.view.favorite

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

private const val POSTER_SIZE = "w342/"
private const val MAIN_POSTER_PATH = "https://image.tmdb.org/t/p/"

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteListViewHolder>() {
    private var favoriteMoviesList = listOf<TmdbMovieByIdDto>()
    private lateinit var mListener: OnItemClickListener

    fun setAdapterData(mFavoriteMoviesList: List<TmdbMovieByIdDto>) {
        favoriteMoviesList = mFavoriteMoviesList
        notifyItemRangeInserted(0, favoriteMoviesList.size)
    }

    fun clearData() {
        notifyItemRangeRemoved(0, favoriteMoviesList.size)
        favoriteMoviesList = listOf()
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int, moviesList: List<TmdbMovieByIdDto>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return FavoriteListViewHolder(itemView, mListener, favoriteMoviesList)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = "\"${
                favoriteMoviesList[position].title
            }\" (${
                favoriteMoviesList[position].releaseDate.substring(0, 4)
            })"

            ratingBackground.setBackgroundColorByRating(favoriteMoviesList[position].voteAverage)
            movieRating.text = favoriteMoviesList[position].voteAverage.toString()

            Picasso.get()
                .load("${MAIN_POSTER_PATH}${POSTER_SIZE}${favoriteMoviesList[position].posterPath}")
                .into(moviePoster)
        }
    }

    override fun getItemCount() = favoriteMoviesList.size

    class FavoriteListViewHolder(
        itemView: View,
        listener: OnItemClickListener,
        favoriteMoviesList: List<TmdbMovieByIdDto>
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClickListener(
                    absoluteAdapterPosition,
                    favoriteMoviesList
                )
            }
        }

        val movieTitle: TextView = itemView.findViewById(R.id.txt_movie_title)
        val movieRating: TextView = itemView.findViewById(R.id.txt_movie_rating)
        val moviePoster: ImageView = itemView.findViewById(R.id.draw_movie_poster)
        val ratingBackground: CardView = itemView.findViewById(R.id.rating_background)
    }
}

