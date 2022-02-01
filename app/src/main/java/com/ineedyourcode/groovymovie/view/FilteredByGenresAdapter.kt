package com.ineedyourcode.groovymovie.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.Movie
import com.squareup.picasso.Picasso

class FilteredByGenresAdapter :
    RecyclerView.Adapter<FilteredByGenresAdapter.ByGenresViewHolder>() {

    private var filteredByGenreList: List<Movie> = listOf()
    private lateinit var mListener: OnItemClickListener
    private val mainPosterPath = "https://image.tmdb.org/t/p/"
    private val posterSize = "w342/"

    fun setAdapterData(receivedGenresList: List<Movie>) {
        notifyDataSetChanged()
        filteredByGenreList = receivedGenresList
    }

    interface OnItemClickListener {
        fun onItemClickListener(position: Int, filteredByGenresList: List<Movie>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ByGenresViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ByGenresViewHolder(itemView, mListener, filteredByGenreList)
    }

    override fun onBindViewHolder(holder: ByGenresViewHolder, position: Int) {
        with(holder) {
            movieTitle.text = filteredByGenreList[position].title
            movieRating.text = filteredByGenreList[position].rating
            movieReleaseDate.text = filteredByGenreList[position].releaseDate

            Picasso.get()
                .load("${mainPosterPath}${posterSize}${filteredByGenreList[position].posterPath}")
                .into(moviePoster)
        }
    }

    override fun getItemCount() = filteredByGenreList.size

    class ByGenresViewHolder(
        itemView: View,
        listener: OnItemClickListener,
        filteredByGenreListFromAdapter: List<Movie>
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClickListener(
                    absoluteAdapterPosition,
                    filteredByGenreListFromAdapter
                )
            }
        }

        val movieTitle: TextView = itemView.findViewById(R.id.txt_movie_title)
        val movieRating: TextView = itemView.findViewById(R.id.txt_movie_rating)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.txt_movie_release_date)
//        val movieGenre: TextView = itemView.findViewById(R.id.txt_movie_genre)
        val moviePoster: ImageView = itemView.findViewById(R.id.draw_movie_poster)
    }
}