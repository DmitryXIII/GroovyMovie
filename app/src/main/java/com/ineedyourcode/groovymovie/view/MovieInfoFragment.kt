package com.ineedyourcode.groovymovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMovieInfoBinding
import com.ineedyourcode.groovymovie.model.Movie

class MovieInfoFragment : Fragment() {

    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_MOVIE = "ARG_MOVIE"
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
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Movie>(ARG_MOVIE)?.let { movie ->
            with(binding) {
                txtMovieInfoTitle.text = movie.title
                txtMovieInfoReleaseDate.text = movie.releaseDate
                txtMovieInfoRating.text = movie.rating
                txtMovieInfoGenre.text = movie.genre
                drawMovieInfoPoster.setImageResource(R.drawable.tmdb_logo)
                txtMovieInfo.text = movie.overview
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}