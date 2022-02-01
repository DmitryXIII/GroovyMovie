package com.ineedyourcode.groovymovie.viewmodel.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieDTO
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.IRetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RemoteDataSource
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.TmdbResponse
import com.ineedyourcode.groovymovie.viewmodel.mainscreen.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class ViewModelRetrofit(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: IRetrofitRepository = RetrofitRepository(RemoteDataSource())
) : ViewModel() {

    fun getData(): MutableLiveData<AppState> {
        getTopRatedFromRemoteSource(65, "ru-RU", 1)
        return liveData
    }

    private val genresSet = mutableSetOf<String>()
    fun getTopRatedFromRemoteSource(id: Int, lang: String, page: Int) {
        liveData.value = AppState.Loading
        retrofitRepository.getGenresList(lang, callbackGenres)
        retrofitRepository.getTopRatedMovies(lang, page, callbackMoviesList)
    }

    private val callbackGenres = object : Callback<TmdbResponse.ResponseGenres> {
        override fun onResponse(
            call: Call<TmdbResponse.ResponseGenres>,
            responseGenresList: Response<TmdbResponse.ResponseGenres>
        ) {
            if (responseGenresList.isSuccessful) {
                val responseBody = responseGenresList.body()

                responseBody?.genres?.forEach { tmdbGenreDTO ->
                    genresSet.add(tmdbGenreDTO.name)
                }

                if (responseBody != null) {
                    Log.d("RepositoryLIVEDATA", "Genres: $genresSet")
                } else {
                    Log.d("Repository", "Failed to get response")
                }
            }
        }

        override fun onFailure(call: Call<TmdbResponse.ResponseGenres>, t: Throwable) {
            Log.e("Repository", "onFailure", t)
        }
    }

    private val callbackMoviesList = object : Callback<TmdbResponse.ResponseMoviesList> {
        override fun onResponse(
            call: Call<TmdbResponse.ResponseMoviesList>,
            responseMoviesList: Response<TmdbResponse.ResponseMoviesList>
        ) {
            val responseBody = responseMoviesList.body()
                if (responseMoviesList.isSuccessful) {
                    if (responseBody != null) {
                     liveData.postValue(checkResponse(responseBody.movies))
                        Log.d("Repository", "Movies: ${responseBody.movies}")
                    } else {
                     liveData.postValue(AppState.Error("Failed to get response"))
                        Log.d("Repository", "Failed to get response")
                    }
                }
        }

        override fun onFailure(call: Call<TmdbResponse.ResponseMoviesList>, t: Throwable) {
            liveData.postValue(AppState.Error("onFailure"))
            Log.e("Repository", "onFailure", t)
        }
    }

    private fun checkResponse(serverResponse: List<TmdbMovieDTO>): AppState {
        return if (serverResponse.isNullOrEmpty()) {
            AppState.Error(CORRUPTED_DATA)
        } else {
            AppState.Success(convertDtoToModel(serverResponse), genresSet)
        }
    }

    private fun convertDtoToModel(serverResponse: List<TmdbMovieDTO>): Map<String, Movie> {
        val moviesMap = mutableMapOf<String, Movie>()
        serverResponse.forEach { movieDTO ->
            val movie = Movie(
                movieDTO.id.toString(),
                movieDTO.title,
                movieDTO.releaseDate,
                movieDTO.voteAverage.toString(),
                movieDTO.genreIds[0].toString(),
                movieDTO.overview
            )
            moviesMap[movieDTO.id.toString()] = movie

        }
        return moviesMap
    }
}