package com.ineedyourcode.groovymovie.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMovieDetailsBinding
import com.ineedyourcode.groovymovie.utils.getImageHeight
import com.ineedyourcode.groovymovie.utils.getImageWidth
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.utils.favoriteMap
import com.ineedyourcode.groovymovie.view.note.NoteFragment
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel
import com.squareup.picasso.Picasso

private const val MAIN_IMAGE_PATH = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w342/"
private const val BACKDROP_SIZE = "w1280/"

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel = FavoriteViewModel()
    private val favoriteList = mutableSetOf<Int>()
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

            if (selectedMovie.overview.isNullOrBlank()) {
                txtMovieOverview.text =
                    getString(R.string.service_movie_overview_request_error_extra)
            } else {
                txtMovieOverview.text = selectedMovie.overview
            }

            Picasso.get()
                .load("${MAIN_IMAGE_PATH}${BACKDROP_SIZE}${selectedMovie.backdropPath}")
                .resize(getImageWidth(), getImageHeight(1.77777))
                .into(drawMovieBackdrop)

            Picasso.get()
                .load("${MAIN_IMAGE_PATH}${POSTER_SIZE}${selectedMovie.posterPath}")
                .into(drawMovieDetailsPoster)


            favoriteViewModel.getAllFavorite().forEach { favoriteEntity ->
                favoriteList.add(favoriteEntity.movieId)
            }

            checkboxFavorite.isChecked = favoriteList.contains(selectedMovie.id)
            checkboxFavorite.setOnClickListener {
                if(checkboxFavorite.isChecked) {
                    favoriteViewModel.saveFavorite(FavoriteEntity(
                        movieId = selectedMovie.id,
                        movieTitle = selectedMovie.title,
                        rating = selectedMovie.rating,
                        posterPath = selectedMovie.posterPath
                    ))
                } else {
                    favoriteViewModel.deleteFavorite(FavoriteEntity(
                        movieId = selectedMovie.id,
                        movieTitle = selectedMovie.title,
                        rating = selectedMovie.rating,
                        posterPath = selectedMovie.posterPath
                    ))
                }
            }
        }

        binding.iconMovieDetailsNote.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(
                    R.id.main_fragment_container,
                    NoteFragment.newInstance(selectedMovie)
                )
                .addToBackStack("")
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}