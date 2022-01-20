package com.ineedyourcode.groovymovie.model

interface IMoviesRepository {
    fun getMoviesMap() : Map<String, Movie>
    fun getGenresList() : List<String>
}