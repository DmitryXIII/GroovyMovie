package com.ineedyourcode.groovymovie.view.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMovieDetailsBinding
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbGenreDTO
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.utils.*
import com.ineedyourcode.groovymovie.view.maps.MapsFragment
import com.ineedyourcode.groovymovie.view.note.NoteFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel
import com.ineedyourcode.groovymovie.viewmodel.MovieDetailsViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

private const val MAIN_IMAGE_PATH = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w342/"
private const val BACKDROP_SIZE = "w1280/"
private const val ACTOR_PHOTO_SIZE = "w185/"
private const val NO_ACTOR_PHOTO_PATH = "https://i.ibb.co/CPDK2sK/ic-no-photo.png"
private const val ACTOR_PHOTO_RATIO = 0.666
private const val BACKDROP_RATIO = 1.777

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel = FavoriteViewModel()
    private val favoriteList = mutableSetOf<Int>()
    private lateinit var selectedMovie: TmdbMovieByIdDTO

    private val viewModel: MovieDetailsViewModel by lazy {
        ViewModelProvider(this)[MovieDetailsViewModel::class.java]
    }

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: TmdbMovieByIdDTO) = MovieDetailsFragment().apply {
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

        viewModel.getMovieById(selectedMovie.id).observe(viewLifecycleOwner, Observer<Any> {
            when (it) {
                is AppState.MovieByIdSuccess -> {
                    getGenres(it.movieDto.genreIds)
                }

                is AppState.ActorsByIdSuccess -> {
                    addActor(it.actorDto)
                }

                is AppState.Error -> {
                    view.showSnackWithoutAction("Ошибка 1")
                }
                is AppState.Loading -> {
//                view.showSnackWithoutAction("LOADING")
                }
                is AppState.FavoriteListSuccess -> {
                    for (entity in it.favoriteList) {
                        this.favoriteList.add(entity.movieId)
                        binding.checkboxFavorite.isChecked = favoriteList.contains(selectedMovie.id)
                    }
                }
                else -> view.showSnackWithoutAction("Ошибка 3")
            }
        })

        with(binding) {
            txtMovieDetailsTitle.text = getString(R.string.movie_details_title, selectedMovie.title)
            txtMovieDetailsReleaseDate.text = selectedMovie.releaseDate
            txtMovieDetailsRating.text = selectedMovie.voteAverage.toString()

            if (selectedMovie.overview.isBlank()) {
                txtMovieOverview.text =
                    getString(R.string.service_movie_overview_request_error_extra)
            } else {
                txtMovieOverview.text =
                    getString(R.string.movie_details_overview, selectedMovie.overview)
            }

            selectedMovie.backdropPath.let {
                Picasso.get()
                    .load("${MAIN_IMAGE_PATH}${BACKDROP_SIZE}${selectedMovie.backdropPath}")
                    .resize(getImageWidth(), getImageHeight(BACKDROP_RATIO))
                    .into(drawMovieBackdrop)
            }

            selectedMovie.posterPath.let {
                Picasso.get()
                    .load("${MAIN_IMAGE_PATH}${POSTER_SIZE}${selectedMovie.posterPath}")
                    .into(drawMovieDetailsPoster)
            }

            checkboxFavorite.setOnClickListener {
                if (checkboxFavorite.isChecked) {
                    favoriteViewModel.saveFavorite(convertMovieToFavoriteEntity(selectedMovie))
                    checkboxFavorite.showSnackWithoutAction("${selectedMovie.title} добавлен в ИЗБРАННЫЕ")
                } else {
                    favoriteViewModel.deleteFavorite(convertMovieToFavoriteEntity(selectedMovie))
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

    private fun getGenres(genreIds: List<TmdbGenreDTO>) {
        for (genreDto in genreIds) {
            if (genreDto == genreIds.last()) {
                binding.txtMovieDetailsGenre.text =
                    "${binding.txtMovieDetailsGenre.text} ${genreDto.name}"
            } else {
                binding.txtMovieDetailsGenre.text =
                    "${binding.txtMovieDetailsGenre.text} ${genreDto.name},"
            }
            Log.d("DETAILSGENRE", "жанр: ${genreDto.name}")
        }

    }

    private fun addActor(actorDto: TmdbActorDto) {
        binding.containerForActors.addView(LayoutInflater.from(requireContext())
            .inflate(R.layout.item_actor, binding.containerForActors, false).apply {
                actorDto.name?.let { name ->
                    this.findViewById<TextView>(R.id.actor_name).text = name
                }

                if (actorDto.birthday == null) {
                    this.findViewById<TextView>(R.id.actor_birthdate).text =
                        getString(R.string.actor_birthdate, getString(R.string.no_information))
                } else {
                    this.findViewById<TextView>(R.id.actor_birthdate).text =
                        getString(R.string.actor_birthdate, actorDto.birthday)
                }

                if (actorDto.birthPlace == null) {
                    this.findViewById<TextView>(R.id.actor_birthplace).text =
                        getString(R.string.actor_birthplace, getString(R.string.no_information))
                } else {
                    this.findViewById<TextView>(R.id.actor_birthplace).text =
                        getString(R.string.actor_birthplace, actorDto.birthPlace)
                }

                val photoWidthInPixels = convertDpToPixels(resources, R.dimen.actor_photo_width)
                val actorPhoto = this.findViewById<ImageView>(R.id.actor_photo)

                Picasso.get()
                    .load("${MAIN_IMAGE_PATH}${ACTOR_PHOTO_SIZE}${actorDto.profilePath}")
                    .resize(
                        photoWidthInPixels,
                        (photoWidthInPixels / ACTOR_PHOTO_RATIO).toInt()
                    )
                    .into(actorPhoto, object : Callback {
                        override fun onSuccess() {
                            actorPhoto.setBackgroundResource(R.drawable.poster_border)
                        }

                        override fun onError(e: java.lang.Exception?) {
                            Picasso.get()
                                .load(NO_ACTOR_PHOTO_PATH)
                                .resize(
                                    photoWidthInPixels,
                                    (photoWidthInPixels / ACTOR_PHOTO_RATIO).toInt()
                                )
                                .into(actorPhoto.also { it.setBackgroundResource(R.drawable.poster_border) })
                        }
                    })

                setOnClickListener {
                    parentFragmentManager
                        .beginTransaction()
                        .add(
                            R.id.main_fragment_container,
                            MapsFragment.newInstance(actorDto.birthPlace.toString())
                        )
                        .addToBackStack("")
                        .commit()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}