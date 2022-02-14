package com.ineedyourcode.groovymovie.view.details

import android.annotation.SuppressLint
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMovieDetailsBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.utils.*
import com.ineedyourcode.groovymovie.view.maps.MapsFragment
import com.ineedyourcode.groovymovie.view.note.NoteFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel
import com.ineedyourcode.groovymovie.viewmodel.RetrofitViewModel
import com.squareup.picasso.Picasso

private const val MAIN_IMAGE_PATH = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w342/"
private const val BACKDROP_SIZE = "w1280/"
private const val PHOTO_SIZE = "w185/"

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel = FavoriteViewModel()
    private val favoriteList = mutableSetOf<Int>()
    private lateinit var selectedMovie: Movie

    private val viewModel: RetrofitViewModel by lazy {
        ViewModelProvider(this)[RetrofitViewModel::class.java]
    }

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

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovieById(selectedMovie.id).observe(viewLifecycleOwner, Observer<Any> {
            renderData(it as AppState)
        })

        with(binding) {
            txtMovieDetailsTitle.text = "\"${selectedMovie.title}\""
            txtMovieDetailsReleaseDate.text = selectedMovie.releaseDate
            txtMovieDetailsRating.text = selectedMovie.rating
            txtMovieDetailsGenre.text = selectedMovie.genre

            if (selectedMovie.overview.isNullOrBlank()) {
                txtMovieOverview.text =
                    getString(R.string.service_movie_overview_request_error_extra)
            } else {
                txtMovieOverview.text = "\"${selectedMovie.overview}\""
            }

            selectedMovie.backdropPath?.let {
                Picasso.get()
                    .load("${MAIN_IMAGE_PATH}${BACKDROP_SIZE}${selectedMovie.backdropPath}")
                    .resize(getImageWidth(), getImageHeight(1.77777))
                    .into(drawMovieBackdrop)
            }

            selectedMovie.posterPath?.let {
                Picasso.get()
                    .load("${MAIN_IMAGE_PATH}${POSTER_SIZE}${selectedMovie.posterPath}")
                    .into(drawMovieDetailsPoster)
            }

            favoriteViewModel.getAllFavorite().forEach { favoriteEntity ->
                favoriteList.add(favoriteEntity.movieId)
            }

            checkboxFavorite.isChecked = favoriteList.contains(selectedMovie.id)
            checkboxFavorite.setOnClickListener {
                if (checkboxFavorite.isChecked) {
                    favoriteViewModel.saveFavorite(
                        FavoriteEntity(
                            movieId = selectedMovie.id,
                            movieTitle = selectedMovie.title,
                            rating = selectedMovie.rating,
                            posterPath = selectedMovie.posterPath,
                            releaseDate = selectedMovie.releaseDate
                        )
                    )
                    checkboxFavorite.showSnackWithoutAction("${selectedMovie.title} добавлен в ИЗБРАННЫЕ")
                } else {
                    favoriteViewModel.deleteFavorite(
                        FavoriteEntity(
                            movieId = selectedMovie.id,
                            movieTitle = selectedMovie.title,
                            rating = selectedMovie.rating,
                            posterPath = selectedMovie.posterPath,
                            releaseDate = selectedMovie.releaseDate
                        )
                    )
                    checkboxFavorite.showSnackWithoutAction("${selectedMovie.title} удален из ИЗБРАННЫХ")
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.ActorsByIdSuccess -> {
                addActor(appState.actorDto)
            }

            is AppState.Error -> {}
        }
    }

    private fun addActor(actorDto: TmdbActorDto) {

        val itemActor = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_actor, binding.containerForActors, false)

        actorDto.name?.let {
            itemActor.findViewById<TextView>(R.id.actor_name).text = it
        }

        if (actorDto.birthday == null) {
            itemActor.findViewById<TextView>(R.id.actor_birthdate).text =
                "Дата рождения: нет информации"
        } else {
            itemActor.findViewById<TextView>(R.id.actor_birthdate).text =
                "Дата рождения: ${actorDto.birthday}"
        }

        if (actorDto.birthPlace == null) {
            itemActor.findViewById<TextView>(R.id.actor_birthplace).text =
                "Место рождения: нет информации"
        } else {
            itemActor.findViewById<TextView>(R.id.actor_birthplace).text =
                "Место рождения: ${actorDto.birthPlace}"
        }

        if (actorDto.profilePath == null) {
            Picasso.get()
                .load("https://i.ibb.co/CPDK2sK/ic-no-photo.png")
                .into(itemActor.findViewById<ImageView>(R.id.actor_photo))
        } else {
            Picasso.get()
                .load("${MAIN_IMAGE_PATH}${PHOTO_SIZE}${actorDto.profilePath}")
                .error(R.drawable.ic_no_photo_2)
                .into(itemActor.findViewById<ImageView>(R.id.actor_photo))
        }

        itemActor.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(
                    R.id.main_fragment_container,
                    MapsFragment.newInstance(actorDto.birthPlace.toString())
                )
                .addToBackStack("")
                .commit()
        }

        binding.containerForActors.addView(itemActor)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}