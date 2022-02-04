package com.ineedyourcode.groovymovie.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.databinding.FragmentMovieDetailsBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.favoriteMap
import com.squareup.picasso.Picasso

private const val MAIN_IMAGE_PATH = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w342/"
private const val BACKDROP_SIZE = "w1280/"

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedMovie: Movie

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: Movie) = MovieDetailsFragment().apply {
            arguments = bundleOf(
                ARG_MOVIE to movie
            )
            selectedMovie = movie
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            txtMovieDetailsTitle.text = "\"${selectedMovie.title}\""
            txtMovieDetailsReleaseDate.text = selectedMovie.releaseDate
            txtMovieDetailsRating.text = selectedMovie.rating
            txtMovieDetailsGenre.text = selectedMovie.genre
            txtMovieOverview.text = selectedMovie.overview

            Picasso.get()
                .load("${MAIN_IMAGE_PATH}${BACKDROP_SIZE}${selectedMovie.backdropPath}")
                .into(drawMovieBackdrop)

            Picasso.get()
                .load("${MAIN_IMAGE_PATH}${POSTER_SIZE}${selectedMovie.posterPath}")
                .into(drawMovieDetailsPoster)

            checkboxFavorite.isChecked = favoriteMap[selectedMovie.id] == true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}