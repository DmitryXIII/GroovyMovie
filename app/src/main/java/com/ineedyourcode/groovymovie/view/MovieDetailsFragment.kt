package com.ineedyourcode.groovymovie.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMovieInfoBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.tmdb.service.MOVIE_ID_EXTRA
import com.ineedyourcode.groovymovie.model.tmdb.service.TMDBMovieOverviewService
import com.ineedyourcode.groovymovie.showSnackWithAction
import com.ineedyourcode.groovymovie.showSnackWithoutAction

const val TMDB_SERVICE_INTENT_FILTER = "TMDB SERVICE INTENT FILTER"
const val TMDB_SERVICE_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val TMDB_SERVICE_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val TMDB_SERVICE_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val TMDB_SERVICE_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val TMDB_SERVICE_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val TMDB_SERVICE_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val TMDB_SERVICE_MOVIE_OVERVIEW_EXTRA = "MOVIE OVERVIEW"

class MovieInfoFragment : Fragment() {

    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedMovie: Movie

    private val loadMovieOverviewReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(TMDB_SERVICE_LOAD_RESULT_EXTRA)) {
                TMDB_SERVICE_RESPONSE_EMPTY_EXTRA -> {
                    view?.showSnackWithoutAction(getString(R.string.service_movie_overview_response_emty_extra))
                    setMovieOverview(getString(R.string.service_movie_overview_response_emty_extra))
                }
                TMDB_SERVICE_RESPONSE_SUCCESS_EXTRA -> {
                    intent.getStringExtra(TMDB_SERVICE_MOVIE_OVERVIEW_EXTRA)?.let { movieOverview ->
                        setMovieOverview(movieOverview)
                    }
                    view?.showSnackWithoutAction(getString(R.string.snack_msg_overview_load_success))
                }
                else -> {
                    setMovieOverview(getString(R.string.service_movie_overview_request_error_extra))
                    view?.showSnackWithAction(
                        getString(R.string.snack_msg_overview_load_error), getString(R.string.retry)
                    ) {
                        getMovieOverview(selectedMovie)
                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: Movie) = MovieInfoFragment().apply {
            arguments = bundleOf(
                ARG_MOVIE to movie
            )
            selectedMovie = movie
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    loadMovieOverviewReceiver,
                    IntentFilter(TMDB_SERVICE_INTENT_FILTER)
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

        with(binding) {
            txtMovieInfoTitle.text = selectedMovie.title
            txtMovieInfoReleaseDate.text = selectedMovie.releaseDate
            txtMovieInfoRating.text = selectedMovie.rating
            txtMovieInfoGenre.text = selectedMovie.genre
            drawMovieInfoPoster.setImageResource(R.drawable.tmdb_logo)
            // для задания № 6 способ получения описания фильма временно изменен
            // на "сервис <-> бродкастресивер"
            // txtMovieInfo.text = movie.overview
            getMovieOverview(selectedMovie) // -> это метод получения описания фильма через "сервис <-> бродкастресивер"
        }

    }

    private fun getMovieOverview(movie: Movie) {
        context?.let {
            it.startService(Intent(it, TMDBMovieOverviewService::class.java).apply {
                putExtra(
                    MOVIE_ID_EXTRA,
                    movie.id
                )
            })
        }
    }

    private fun setMovieOverview(movieOverview: String) {
        binding.txtMovieInfo.text = movieOverview
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadMovieOverviewReceiver)
        }

        _binding = null
    }
}