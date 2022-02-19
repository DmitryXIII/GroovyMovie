package com.ineedyourcode.groovymovie.view.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.squareup.picasso.Picasso

private const val POSTER_SIZE = "w342/"
private const val MAIN_POSTER_PATH = "https://image.tmdb.org/t/p/"

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteListViewHolder>() {
    private var favoriteMoviesList = mutableListOf<FavoriteEntity>()

    fun setAdapterData(mFavoriteMoviesList: List<FavoriteEntity>) {
        favoriteMoviesList = mFavoriteMoviesList as MutableList<FavoriteEntity>
        notifyItemRangeInserted(0, favoriteMoviesList.size)
    }

    fun clearData() {
        favoriteMoviesList = mutableListOf()
        notifyItemRangeRemoved(0, favoriteMoviesList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return FavoriteListViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = "\"${
                favoriteMoviesList[position].movieTitle
            }\" (${
                favoriteMoviesList[position].releaseDate?.substring(0, 4)
            })"
            movieRating.text = favoriteMoviesList[position].rating

            Picasso.get()
                .load("${MAIN_POSTER_PATH}${POSTER_SIZE}${favoriteMoviesList[position].posterPath}")
                .into(moviePoster)
        }
    }

    override fun getItemCount() = favoriteMoviesList.size

    class FavoriteListViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        val movieTitle: TextView = itemView.findViewById(R.id.txt_movie_title)
        val movieRating: TextView = itemView.findViewById(R.id.txt_movie_rating)
        val moviePoster: ImageView = itemView.findViewById(R.id.draw_movie_poster)
    }
}

