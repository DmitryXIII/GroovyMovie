package com.ineedyourcode.groovymovie.model

interface IMoviesRepository {
    fun getMoviesList() : List<Movie>
}