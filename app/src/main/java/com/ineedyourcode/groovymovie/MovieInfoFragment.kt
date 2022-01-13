package com.ineedyourcode.groovymovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class MovieInfoFragment : Fragment() {

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"

        fun newInstance(movie: Movie) = MovieInfoFragment().apply {
            arguments = bundleOf(
                ARG_MOVIE to movie
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val movieResult: Movie? = arguments?.getParcelable(ARG_MOVIE)
            val txtMovieTitle: TextView = view.findViewById(R.id.txt_movie_info_title)
            val txtMovieReleaseDate: TextView = view.findViewById(R.id.txt_movie_info_release_date)
            val txtMovieGenre: TextView = view.findViewById(R.id.txt_movie_info_genre)
            val txtMovieRating: TextView = view.findViewById(R.id.txt_movie_info_rating)
            val imgPoster: ImageView = view.findViewById(R.id.draw_movie_info_poster)

            txtMovieTitle.text = movieResult?.title
            txtMovieReleaseDate.text = movieResult?.releaseDate
            txtMovieRating.text = movieResult?.rating
            txtMovieGenre.text = movieResult?.genre
            if (movieResult != null) {
                imgPoster.setImageResource(movieResult.poster)
            }
        }
    }
}