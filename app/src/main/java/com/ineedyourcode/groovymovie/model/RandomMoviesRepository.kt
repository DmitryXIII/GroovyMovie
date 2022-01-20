package com.ineedyourcode.groovymovie.model

import com.ineedyourcode.groovymovie.R
import kotlin.random.Random

class RandomMoviesRepository : IMoviesRepository {

    private val moviesList = mutableListOf<Movie>()

    init {
        (0..99).forEach { i ->
            moviesList.add(
                Movie(
                    "Movie_$i" + "_название фильма",
                    (1990..2022).random().toString(),
                    (Random.nextDouble(1.0, 5.0)).toString().substring(0, 3), randomGenre(),
                    R.drawable.tmdb_logo
                )
            )
        }
    }

    private fun randomGenre(): String {
        return when((1..5).random()) {
            1 -> "Комедия"
            2 -> "Боевик"
            3 -> "Триллер"
            4 -> "Драма"
            5 -> "Семейный"
            else -> ""
        }
    }

    override fun getMoviesList(): List<Movie> = moviesList
}
