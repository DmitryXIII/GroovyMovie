package com.ineedyourcode.groovymovie.model

interface IMoviesRepository {
    fun loadData()
    fun getMoviesMap() : Map<Int, Movie>
    fun getGenresList() : Set<String>
}