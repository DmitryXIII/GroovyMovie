package com.ineedyourcode.groovymovie.model.tmdb.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
//    private val movieListAPI = Retrofit.Builder()
//        .baseUrl("https://api.themoviedb.org/3/")
//        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
//        .build().create(MovieAPI::class.java)

    private val movieListAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build().create(MovieAPI::class.java)

    fun getTopRatedList(lang: String, page: Int, callback: Callback<TmdbResponse.ResponseMoviesList>) {
        movieListAPI.getTopRated(lang = lang, page = page).enqueue(callback)
//        object : Callback<TmdbResponse.ResponseMoviesList> {
//            override fun onResponse(
//                call: Call<TmdbResponse.ResponseMoviesList>,
//                responseMoviesList: Response<TmdbResponse.ResponseMoviesList>
//            ) {
//                if (responseMoviesList.isSuccessful) {
//                    val responseBody = responseMoviesList.body()
//
//                    if (responseBody != null) {
//                        Log.d("Repository", "Movies: ${responseBody.movies}")
//                    } else {
//                        Log.d("Repository", "Failed to get response")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<TmdbResponse.ResponseMoviesList>, t: Throwable) {
//                Log.e("Repository", "onFailure", t)
//            }
//        })
    }


    fun getGenresList(lang: String, callback: Callback<TmdbResponse.ResponseGenres>) {
        movieListAPI.getGenres(lang = lang).enqueue(callback)
//        object : Callback<TmdbResponse.ResponseGenres> {
//            override fun onResponse(
//                call: Call<TmdbResponse.ResponseGenres>,
//                responseGenresList: Response<TmdbResponse.ResponseGenres>
//            ) {
//                if (responseGenresList.isSuccessful) {
//                    val responseBody = responseGenresList.body()
//
//                    if (responseBody != null) {
//                        Log.d("Repository", "Genres: ${responseBody.genres}")
//                    } else {
//                        Log.d("Repository", "Failed to get response")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<TmdbResponse.ResponseGenres>, t: Throwable) {
//                Log.e("Repository", "onFailure", t)
//            }
//        })
    }

    fun getMovie(id: Int, lang: String) {
        movieListAPI.getMovie(id = id, lang = lang).enqueue(object : Callback<TmdbMovieDTO> {
            override fun onResponse(
                call: Call<TmdbMovieDTO>,
                responseMovie: Response<TmdbMovieDTO>
            ) {
                if (responseMovie.isSuccessful) {
                    val responseBody = responseMovie.body()

                    if (responseBody != null) {
                        Log.d("Repository", "Movie: $responseBody")
                    } else {
                        Log.d("Repository", "Failed to get response")
                    }
                }
            }

            override fun onFailure(call: Call<TmdbMovieDTO>, t: Throwable) {
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
