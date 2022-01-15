package com.ineedyourcode.groovymovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.databinding.FragmentMovieInfoBinding
import com.ineedyourcode.groovymovie.model.Movie

class MovieInfoFragment : Fragment() {

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"

        private var _binding: FragmentMovieInfoBinding? = null
        private val binding get() = _binding!!

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

        val movieFromArguments: Movie? = arguments?.getParcelable(ARG_MOVIE)
        if (movieFromArguments != null) {
            val txtMovieTitle = binding.txtMovieInfoTitle
            val txtMovieReleaseDate = binding.txtMovieInfoReleaseDate
            val txtMovieGenre = binding.txtMovieInfoGenre
            val txtMovieRating = binding.txtMovieInfoRating
            val imgPoster = binding.drawMovieInfoPoster

            txtMovieTitle.text = movieFromArguments.title
            txtMovieReleaseDate.text = movieFromArguments.releaseDate
            txtMovieRating.text = movieFromArguments.rating
            txtMovieGenre.text = movieFromArguments.genre
            imgPoster.setImageResource(movieFromArguments.poster)
        }
    }
}