package com.ineedyourcode.groovymovie.model

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.ineedyourcode.groovymovie.BuildConfig
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class TMDBRepository {


    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() {
        try {
            val uriGenres =
                URL("https://api.themoviedb.org/3/genre/movie/list?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU")
            val uriTopRated =
                URL("https://api.themoviedb.org/3/movie/top_rated?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&page=10")
            val handler = Handler()

            Thread(Runnable {
                lateinit var urlTopRatedConnection: HttpsURLConnection
                lateinit var urlGenresListConnection: HttpsURLConnection
                try {
                    urlTopRatedConnection = uriTopRated.openConnection() as HttpsURLConnection
                    urlTopRatedConnection.requestMethod = "GET"
                    urlTopRatedConnection.addRequestProperty("Content-Type", "application/json")
                    urlTopRatedConnection.readTimeout = 10000
                    val bufferedReaderTopRated =
                        BufferedReader(InputStreamReader(urlTopRatedConnection.inputStream))

                    urlGenresListConnection = uriGenres.openConnection() as HttpsURLConnection
                    urlGenresListConnection.requestMethod = "GET"
                    urlGenresListConnection.addRequestProperty("Content-Type", "application/json")
                    urlGenresListConnection.readTimeout = 10000

                    val bufferedReaderGenresList =
                        BufferedReader(InputStreamReader(urlGenresListConnection.inputStream))

                    // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
//                    var moviesDTOList: Array<MovieDTO>


//                    var moviesDTOList: Array<MovieDTO> = Gson().fromJson(getLines(bufferedReader), Array<MovieDTO>::class.java)

                    println("===========================================================================================================")

                    val movieList = parseResult(getLines(bufferedReaderTopRated))
                    val genresMap = parseGenres(getLines(bufferedReaderGenresList))
                    for (movieDTO in movieList) {
                        println(movieDTO)
                    }
                    for (entry in genresMap) {
                        println(entry)
                    }

//                    handler.post { listener.onLoaded(weatherDTO) }
                } catch (e: Exception) {
                    Log.e("111", "Fail connection", e)
                    e.printStackTrace()
//                    listener.onFailed(e)
                } finally {
                    urlTopRatedConnection.disconnect()
                    urlGenresListConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
//            listener.onFailed(e)
        }
    }


    private fun parseResult(result: String): List<MovieDTO> {
        val moviesDTOList = mutableListOf<MovieDTO>()
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

                val movieDTO = MovieDTO(
                    adult = jsonMovie.getBoolean("adult"),
                    backdrop_path = jsonMovie.getString("backdrop_path"),
                    genre_ids = genreArr,
                    id = jsonMovie.getInt("id"),
                    original_language = jsonMovie.getString("original_language"),
                    original_title = jsonMovie.getString("original_title"),
                    overview = jsonMovie.getString("overview"),
                    popularity = jsonMovie.getDouble("popularity"),
                    poster_path = jsonMovie.getString("poster_path"),
                    release_date = jsonMovie.getString("release_date"),
                    title = jsonMovie.getString("title"),
                    video = jsonMovie.getBoolean("video"),
                    vote_average = jsonMovie.getDouble("vote_average"),
                    vote_count = jsonMovie.getInt("id")
                )
                moviesDTOList.add(movieDTO)
            }
        } catch (e: JSONException) {
            Log.d("DEBUG_TAG", "Error parsing JSON. String was: $result")
        }
        return moviesDTOList
    }

    fun parseGenres(result: String): Map<Int, String> {
        val mapOfGenres = mutableMapOf<Int, String>()
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
        return mapOfGenres
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}