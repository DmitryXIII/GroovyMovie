package com.ineedyourcode.groovymovie.view.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.utils.favoriteMap
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel
import com.squareup.picasso.Picasso

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteListViewHolder>() {
    private lateinit var favoriteMoviesList: List<FavoriteEntity>
    private val viewModel = FavoriteViewModel()
    private val mainPosterPath = "https://image.tmdb.org/t/p/"
    private val posterSize = "w342/"
    private val favoriteList = mutableSetOf<Int>()

    fun setAdapterData(mFavoriteMoviesList: List<FavoriteEntity>) {
        viewModel.getAllFavorite().forEach { favoriteEntity ->
            favoriteList.add(favoriteEntity.movieId)
        }

        favoriteMoviesList = mFavoriteMoviesList
        notifyItemRangeChanged(0, favoriteMoviesList.size)
    }

    fun clearData() {
        notifyItemRangeRemoved(0, favoriteMoviesList.size)
        favoriteMoviesList = listOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return FavoriteListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = favoriteMoviesList[position].movieTitle
            movieRating.text = favoriteMoviesList[position].rating

            isFavorite.isChecked = favoriteList.contains(favoriteMoviesList[position].movieId)

            isFavorite.setOnClickListener(View.OnClickListener {
                if (isFavorite.isChecked) {
                    isFavorite.showSnackWithoutAction("${movieTitle.text} добавлен в ИЗБРАННЫЕ")
                    viewModel.saveFavorite(favoriteMoviesList[position])
                    favoriteMap[favoriteMoviesList[position].movieId] = true
                } else {
                    viewModel.deleteFavorite(favoriteMoviesList[position])
                    isFavorite.showSnackWithoutAction("${movieTitle.text} удален из ИЗБРАННЫХ")
                    favoriteMap[favoriteMoviesList[position].movieId] = false
                }
            })

            Picasso.get()
                .load("${mainPosterPath}${posterSize}${favoriteMoviesList[position].posterPath}")
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
        val isFavorite: CheckBox = itemView.findViewById(R.id.checkbox_favorite)
    }
}

