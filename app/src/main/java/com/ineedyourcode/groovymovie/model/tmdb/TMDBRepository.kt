package com.ineedyourcode.groovymovie.model.tmdb

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.model.IMoviesRepository
import com.ineedyourcode.groovymovie.model.Movie
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val TMDB_REPOSITORY_TAG = "TMDB_REPOSITORY_TAG"

@RequiresApi(Build.VERSION_CODES.N)
class TMDBRepository : IMoviesRepository {

    private lateinit var mapOfGenres: Map<Int, String>
    private val topRatedMoviesMap = mutableMapOf<String, Movie>()
    private val parser = TMDBJsonParser()

    override fun loadData() {
        try {
            val uriGenres =
                URL("https://api.themoviedb.org/3/genre/movie/list?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU")
            val urlGenresListConnection = (uriGenres.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 10000
                disconnect()
            }

            val bufferedReaderGenresList =
                BufferedReader(InputStreamReader(urlGenresListConnection.inputStream))

            mapOfGenres = parser.parseGenresList(getLines(bufferedReaderGenresList))

            // TMDB в одном запросе отдает только 1 страницу (20 фильмов).
            // Цикл необходим для получения данных сразу с нескольких страниц
            for (page in 1..10) {
                val uriTopRated =
                    URL("https://api.themoviedb.org/3/movie/top_rated?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&page=$page")
                val urlTopRatedConnection =
                    (uriTopRated.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        disconnect()
                    }

                val bufferedReaderTopRated =
                    BufferedReader(InputStreamReader(urlTopRatedConnection.inputStream))

                // парсер обрабатывает данные одной страницы,
                // поэтому при каждой итерации в topRatedMoviesMap постранично добавляются данные от парсера
                parser.parseTopRatedList(getLines(bufferedReaderTopRated), mapOfGenres)
                    .forEach { (movieId, movie) ->
                        topRatedMoviesMap[movieId] = movie
                    }
            }
        } catch (e: Exception) {
            Log.e(TMDB_REPOSITORY_TAG, e.toString())
            throw Exception("Connection failed")
        }
    }

    private fun getLines(reader: BufferedReader) = reader.lines().collect(Collectors.joining("\n"))

    override fun getMoviesMap(): Map<String, Movie> = topRatedMoviesMap

    override fun getGenresList(): Set<String> {
        val setOfGenres = mutableSetOf<String>()
        topRatedMoviesMap.values.forEach { movie ->
            setOfGenres.add(movie.genre.toString())
        }
        return setOfGenres
    }
}