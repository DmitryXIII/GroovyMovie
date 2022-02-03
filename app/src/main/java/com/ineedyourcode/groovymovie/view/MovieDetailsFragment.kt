package com.ineedyourcode.groovymovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.databinding.FragmentMovieDetailsBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.squareup.picasso.Picasso

private const val MAIN_POSTER_PATH = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w500/"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            txtMovieInfoTitle.text = selectedMovie.title
            txtMovieInfoReleaseDate.text = selectedMovie.releaseDate
            txtMovieInfoRating.text = selectedMovie.rating
            txtMovieInfoGenre.text = selectedMovie.genre
            txtMovieInfo.text = selectedMovie.overview
            Picasso.get()
                .load("${MAIN_POSTER_PATH}${POSTER_SIZE}${selectedMovie.posterPath}")
                .into(drawMovieInfoPoster)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}