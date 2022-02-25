package com.ineedyourcode.groovymovie.view.movieslist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.utils.convertMovieToFavoriteEntity
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.viewmodel.MoviesListViewModel
import com.squareup.picasso.Picasso

class MoviesListAdapter :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {

    private lateinit var moviesList: List<TmdbMovieByIdDTO>
    private lateinit var mListener: OnItemClickListener
    private val viewModel = MoviesListViewModel()
    private val favoriteList = mutableSetOf<Int>()
    private val mainPosterPath = "https://image.tmdb.org/t/p/"
    private val posterSize = "w342/"

    fun setAdapterData(moviesListFromFragment: List<TmdbMovieByIdDTO>) {
        moviesList = moviesListFromFragment
        notifyItemRangeChanged(0, moviesList.size)
    }

    fun setFavoriteList(favoriteListFromFragment: List<FavoriteEntity>) {
        for (entity in favoriteListFromFragment) {
            favoriteList.add(entity.movieId)
        }
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int, moviesList: List<TmdbMovieByIdDTO>)
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
            movieRating.text = moviesList[position].voteAverage.toString()

            // если фильм есть в списке избранных - установить флажок "избранное"
            isFavorite.isChecked = favoriteList.contains(moviesList[position].id)

            isFavorite.setOnClickListener(View.OnClickListener {
                if (isFavorite.isChecked) {
                    isFavorite.showSnackWithoutAction("${movieTitle.text} добавлен в ИЗБРАННЫЕ")
                    viewModel.saveFavorite(convertMovieToFavoriteEntity(moviesList[position]))
                } else {
                    viewModel.deleteFavorite(convertMovieToFavoriteEntity(moviesList[position]))
                    isFavorite.showSnackWithoutAction("${movieTitle.text} удален из ИЗБРАННЫХ")
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
        moviesList: List<TmdbMovieByIdDTO>
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