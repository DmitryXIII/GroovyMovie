package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import android.util.Log
import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.model.tmdb.TMDBMovieDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    private val movieAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(MovieAPI::class.java)

    fun getTopRatedList(lang: String, page: Int) {
        movieAPI.getTopRated(lang = lang, page = page).enqueue(object : Callback<TMDBResponse> {
            override fun onResponse(
                call: Call<TMDBResponse>,
                response: Response<TMDBResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        Log.d("Repository", "Movies: ${responseBody.movies}")
                    } else {
                        Log.d("Repository", "Failed to get response")
                    }
                }
            }

            override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                Log.e("Repository", "onFailure", t)
            }
        })
    }
}

/*
fun getPopularMovies(page: Int = 1) {
    api.getPopularMovies(page = page)
        .enqueue(object : Callback<GetMoviesResponse> {
            override fun onResponse(
                call: Call<GetMoviesResponse>,
                response: Response<GetMoviesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        Log.d("Repository", "Movies: ${responseBody.movies}")
                    } else {
                        Log.d("Repository", "Failed to get response")
                    }
                }
            }

            override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                Log.e("Repository", "onFailure", t)
            }
        })
}*/
