package com.ineedyourcode.groovymovie.model.tmdb

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.model.IMoviesRepository
import com.ineedyourcode.groovymovie.model.Movie
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)
class TMDBRepository : IMoviesRepository {

    private val mapOfGenres = mutableMapOf<Int, String>()
    private val moviesMap = mutableMapOf<String, Movie>()

    init {
        try {
            Thread {
                lateinit var urlTopRatedConnection: HttpsURLConnection
                lateinit var urlGenresListConnection: HttpsURLConnection
                try {
                    val uriGenres =
                        URL("https://api.themoviedb.org/3/genre/movie/list?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU")
                    urlGenresListConnection = uriGenres.openConnection() as HttpsURLConnection
                    urlGenresListConnection.apply {
                        requestMethod = "GET"
//                        addRequestProperty("Content-Type", "application/json")
                        readTimeout = 10000
                    }

                    val bufferedReaderGenresList =
                        BufferedReader(InputStreamReader(urlGenresListConnection.inputStream))

                    parseGenres(getLines(bufferedReaderGenresList))
                    for (i in 1..15) {
                        val uriTopRated =
                            URL("https://api.themoviedb.org/3/movie/top_rated?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&page=$i")
                        urlTopRatedConnection = uriTopRated.openConnection() as HttpsURLConnection
                        urlTopRatedConnection.apply {
                            requestMethod = "GET"
//                            addRequestProperty("Content-Type", "application/json")
                            readTimeout = 10000
                        }

                        val bufferedReaderTopRated =
                            BufferedReader(InputStreamReader(urlTopRatedConnection.inputStream))

                        parseTopRatedList(getLines(bufferedReaderTopRated))
                    }

//                    var moviesDTOList: Array<MovieDTO> = Gson().fromJson(getLines(bufferedReader), Array<MovieDTO>::class.java)
                } catch (e: Exception) {
                    Log.e("111", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlTopRatedConnection.disconnect()
                    urlGenresListConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
        }
    }

    private fun parseTopRatedList(result: String) {
        val moviesDTOList = mutableListOf<TMDBMovieDTO>()
        try {
            val jsonObj = JSONObject(result)
            val array = jsonObj.getJSONArray("results")

            for (i in 0 until array.length()) {
                val jsonMovie = array.getJSONObject(i)
                var genreArr = IntArray(0)

                jsonMovie.optJSONArray("genre_ids")?.let { genresIds ->
                    genreArr = IntArray(genresIds.length())
                    for (i in 0 until genresIds.length()) {
                        genreArr[i] = genresIds.getInt(i)
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
                moviesDTOList.add(movieDTO)
            }

            moviesDTOList.forEach { movieDTO ->
                val movie = Movie(
                    movieDTO.id.toString(),
                    movieDTO.title,
                    movieDTO.releaseDate,
                    movieDTO.voteAverage.toString(),
                    mapOfGenres[movieDTO.genreIds[0]],
                    movieDTO.overview,
                    R.drawable.tmdb_logo
                )
                moviesMap[movie.id.toString()] = movie
            }
        } catch (e: JSONException) {
            Log.d("DEBUG_TAG", "Error parsing JSON. String was: $result")
        }
    }

    private fun parseGenres(result: String) {
        try {
            val jsonObj = JSONObject(result)
            val array = jsonObj.getJSONArray("genres")

            for (i in 0 until array.length()) {
                val genre = array.getJSONObject(i)
                mapOfGenres[genre.getInt("id")] = genre.getString("name")
            }
        } catch (e: JSONException) {
            Log.d("DEBUG_TAG", "Error parsing JSON. String was: $result")
        }
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    override fun getMoviesMap(): Map<String, Movie> = moviesMap

    override fun getGenresList(): Set<String> {
        val setOfGenres = mutableSetOf<String>()

        moviesMap.values.forEach { movie ->
            setOfGenres.add(movie.genre.toString())
        }

        return setOfGenres
    }
}