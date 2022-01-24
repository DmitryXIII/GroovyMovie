package com.ineedyourcode.groovymovie.model

data class MovieDTO(
    private val adult: Boolean,
    private val backdrop_path: String,
    private val genre_ids: IntArray,
    private val id: Int,
    private val original_language: String,
    private val original_title: String,
    private val overview: String,
    private val popularity: Double,
    private val poster_path: String,
    private val release_date: String,
    private val title:  String,
    private val video: Boolean,
    private val vote_average: Double,
    private val vote_count: Int
)