package com.ineedyourcode.groovymovie.model

interface IMoviesRepository {
    fun loadData()
    fun getMoviesMap() : Map<String, Movie>
    fun getGenresList() : Set<String>
}