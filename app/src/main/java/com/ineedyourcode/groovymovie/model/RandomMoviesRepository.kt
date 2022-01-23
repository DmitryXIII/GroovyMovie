package com.ineedyourcode.groovymovie.model

import com.ineedyourcode.groovymovie.R
import kotlin.random.Random

class RandomMoviesRepository : IMoviesRepository {

    private val moviesList = mutableListOf<Movie>()

    init {
        (0..19).forEach { i ->
            moviesList.add(
                Movie(
                    "Movie_$i" + "_название фильма/сериала",
                    (2000 + i).toString(),
                    (Random.nextDouble(1.0, 5.0)).toString().substring(0, 3),
                    "Какой-то жанр",
                    R.drawable.tmdb_logo
                )
            )
        }
    }

    override fun getMoviesList(): List<Movie> {
        return this.moviesList
    }
}
