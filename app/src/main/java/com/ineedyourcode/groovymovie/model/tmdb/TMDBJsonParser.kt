package com.ineedyourcode.groovymovie.model.tmdb

import android.util.Log
import com.ineedyourcode.groovymovie.model.Movie
import org.json.JSONException
import org.json.JSONObject

class TMDBJsonParser {
    private val parserTag = "TMDB_Json_parser_TAG"
    private val mapOfGenres = mutableMapOf<Int, String>()

    fun parseGenresList(requestResult: String): Map<Int, String> {
        try {
            val jsonObj = JSONObject(requestResult)
            val array = jsonObj.getJSONArray("genres")

            for (i in 0 until array.length()) {
                val genre = array.getJSONObject(i)
                mapOfGenres[genre.getInt("id")] = genre.getString("name")
            }
        } catch (e: JSONException) {
            Log.e(parserTag, "Error parsing genres from JSON: $requestResult")
        }
        return mapOfGenres
    }

    fun parseTopRatedList(result: String, mapOfGenres: Map<Int, String>): Map<String, Movie> {
        val topRatedMoviesMap = mutableMapOf<String, Movie>()
        try {
            val jsonObj = JSONObject(result)
            val array = jsonObj.getJSONArray("results")

            for (i in 0 until array.length()) {
                val jsonMovie = array.getJSONObject(i)
                var genreArr = IntArray(0)

                jsonMovie.optJSONArray("genre_ids")?.let { genresIds ->
                    genreArr = IntArray(genresIds.length())
                    for (index in 0 until genresIds.length()) {
                        genreArr[index] = genresIds.getInt(index)
                    }
                }

                val movieDTO = TMDBMovieDTO(
                    adult = jsonMovie.getBoolean("adult"),
                    backdropPath = jsonMovie.getString("backdrop_path"),
                    genreIds = genreArr,
                    id = jsonMovie.getInt("id"),
                    originalLanguage = jsonMovie.getString("original_language"),
                    originalTitle = jsonMovie.getString("original_title"),
                    overview = jsonMovie.getString("overview"),
                    popularity = jsonMovie.getDouble("popularity"),
                    posterPath = jsonMovie.getString("poster_path"),
                    releaseDate = jsonMovie.getString("release_date"),
                    title = jsonMovie.getString("title"),
                    video = jsonMovie.getBoolean("video"),
                    voteAverage = jsonMovie.getDouble("vote_average"),
                    voteCount = jsonMovie.getInt("id")
                )

                val movie = Movie(
                    movieDTO.id.toString(),
                    movieDTO.title,
                    movieDTO.releaseDate,
                    movieDTO.voteAverage.toString(),
                    mapOfGenres[movieDTO.genreIds[0]],
                    movieDTO.overview
                )

                topRatedMoviesMap[movie.id.toString()] = movie
            }
        } catch (e: JSONException) {
            Log.e(parserTag, "Error parsing movies from JSON: $result")
        }
        return topRatedMoviesMap
    }
}