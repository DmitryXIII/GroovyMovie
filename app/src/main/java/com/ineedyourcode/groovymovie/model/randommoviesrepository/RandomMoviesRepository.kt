package com.ineedyourcode.groovymovie.model.randommoviesrepository

import com.ineedyourcode.groovymovie.model.IMoviesRepository
import com.ineedyourcode.groovymovie.model.Movie
import java.util.*
import kotlin.random.Random

class RandomMoviesRepository : IMoviesRepository {

    private val genresSet = mutableSetOf<String>()
    private val moviesMap = mutableMapOf<Int, Movie>()

    override fun loadData() {
        (0..300).forEach { i ->
            val movie = Movie(
                i,
                "Movie_$i" + "_название фильма",
                (1990..2022).random().toString(),
                (Random.nextDouble(1.0, 5.0)).toString().substring(0, 3), randomGenre(),
                "Описание...",
                "1qp6p7IXXYQ6vTiQ3Fd5GMpD4mK.jpg",
                "1qp6p7IXXYQ6vTiQ3Fd5GMpD4mK.jpg",
                false
            )

            moviesMap[movie.id] = movie
            genresSet.add(movie.genre.toString())
        }
    }

    private fun randomGenre(): String {
        return when ((1..10).random()) {
            1 -> "Комедия"
            2 -> "Боевик"
            3 -> "Триллер"
            4 -> "Драма"
            5 -> "Семейный"
            6 -> "Ужасы"
            7 -> "Документальный"
            8 -> "Исторический"
            9 -> "Мюзикл"
            10 -> "Мелодрама"
            else -> "Другой жанр"
        }
    }

    override fun getMoviesMap(): Map<Int, Movie> = moviesMap
    override fun getGenresList(): Set<String> = genresSet
}