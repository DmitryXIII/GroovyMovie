package com.ineedyourcode.groovymovie.model.tmdb.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.model.tmdb.httpsurlconnection.TmdbJsonParser
import com.ineedyourcode.groovymovie.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MOVIE_ID_EXTRA = "MOVIE ID"
private const val TMDB_MOVIE_OVERVIEW_SERVICE_TAG = "TMDB_MOVIE_OVERVIEW_SERVICE_TAG"

class TmdbMovieOverviewService(name: String = "TMDBMovieOverviewService") : IntentService(name) {
    private val broadcastIntent = Intent(TMDB_SERVICE_INTENT_FILTER)
    private val parser = TmdbJsonParser()
    private lateinit var movieOverview: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            intent.getStringExtra(MOVIE_ID_EXTRA)?.let { movieId ->
                loadMovieOverview(movieId)
            }
        }
    }

    // поиск в TMDB фильма по ID, полученному через intent
    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovieOverview(movieId: String) {
        try {
            val uriMovieOverview =
                URL("https://api.themoviedb.org/3/movie/${movieId}?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU")
            lateinit var urlMovieOverviewConnection: HttpsURLConnection
            try {
                urlMovieOverviewConnection =
                    (uriMovieOverview.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }

                val bufferedReaderMovieOverview =
                    BufferedReader(InputStreamReader(urlMovieOverviewConnection.inputStream))

                movieOverview = parser.parseMovieOverview(getLines(bufferedReaderMovieOverview))
                onResponse(movieOverview)
            } catch (e: Exception) {
                onErrorRequest(e.toString())
                Log.e(TMDB_MOVIE_OVERVIEW_SERVICE_TAG, e.toString())
            } finally {
                urlMovieOverviewConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
            Log.e(TMDB_MOVIE_OVERVIEW_SERVICE_TAG, e.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader) = reader.lines().collect(Collectors.joining("\n"))

    private fun onResponse(movieOverview: String) {
        if (movieOverview.isBlank()) {
            onEmptyResponse()
        } else {
            onSuccessResponse(movieOverview)
        }
    }

    private fun onSuccessResponse(movieOverview: String) {
        putLoadResult(TMDB_SERVICE_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(TMDB_SERVICE_MOVIE_OVERVIEW_EXTRA, movieOverview)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(TMDB_SERVICE_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(TMDB_SERVICE_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(TMDB_SERVICE_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(TMDB_SERVICE_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(TMDB_SERVICE_LOAD_RESULT_EXTRA, result)
    }
}