package com.ineedyourcode.groovymovie

interface IMoviesRepository {
    fun getMoviesList() : List<Movie>
}