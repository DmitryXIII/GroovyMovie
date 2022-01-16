package com.ineedyourcode.groovymovie.model

import com.ineedyourcode.groovymovie.R
import kotlin.random.Random

class RandomMoviesRepository : IMoviesRepository {

    private val moviesList = mutableListOf<Movie>()
    private val moviesComedyList = mutableListOf<Movie>()
    private val moviesDramaList = mutableListOf<Movie>()
    private val moviesActionList = mutableListOf<Movie>()
    private val moviesFamilyList = mutableListOf<Movie>()
    private val moviesTrillerList = mutableListOf<Movie>()

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
            when(moviesList[i].genre) {
                "Комедия" -> moviesComedyList.add(moviesList[i])
                "Боевик" -> moviesActionList.add(moviesList[i])
                "Триллер" -> moviesTrillerList.add(moviesList[i])
                "Драма" -> moviesDramaList.add(moviesList[i])
                "Семейный" -> moviesFamilyList.add(moviesList[i])
            }
        }
    }

    private fun randomGenre(): String {
        when((1..5).random()) {
            1 -> return "Комедия"
            2 -> return "Боевик"
            3 -> return "Триллер"
            4 -> return "Драма"
            5 -> return "Семейный"
        }
        return ""
    }

    override fun getMoviesList(): List<Movie> {
        return this.moviesList
    }
}
