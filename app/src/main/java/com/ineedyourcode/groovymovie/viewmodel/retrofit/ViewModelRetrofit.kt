package com.ineedyourcode.groovymovie.viewmodel.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.model.tmdb.TmdbMovieFromListDTO
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.IRetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RemoteDataSource
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.TmdbResponse
import com.ineedyourcode.groovymovie.viewmodel.mainscreen.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val CORRUPTED_DATA = "Неполные данные"
private const val RESPONSE_GENRES_ERROR = "Response genres failed"
private const val RESPONSE_MOVIES_LIST_ERROR = "Failed to get response movies list"
private const val RESPONSE_MOVIE_BY_ID_ERROR = "Response movie by id failed"
private const val TAG = "RETROFIT_VIEW_MODEL"

/**
 * ViewModelRetrofit используется для получения данных через Retrofit
 */
class ViewModelRetrofit(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: IRetrofitRepository = RetrofitRepository(RemoteDataSource())
) : ViewModel() {

    private val genresMap = mutableMapOf<Int, String>()
    private val moviesMap = mutableMapOf<String, Movie>()

    init {
        // обработка ответа с сервера на запрос списка жанров, имеющихся в TMDB
        val callbackGenres = object : Callback<TmdbResponse.ResponseGenres> {
            override fun onResponse(
                call: Call<TmdbResponse.ResponseGenres>,
                responseGenresList: Response<TmdbResponse.ResponseGenres>
            ) {
                val responseBody = responseGenresList.body()
                if (responseGenresList.isSuccessful) {
                    responseBody?.genres?.forEach { tmdbGenreDTO ->
                        genresMap[tmdbGenreDTO.id] = tmdbGenreDTO.name
                    }
                }
            }

            override fun onFailure(call: Call<TmdbResponse.ResponseGenres>, t: Throwable) {
                Log.e(TAG, RESPONSE_GENRES_ERROR, t)
            }
        }
        retrofitRepository.getGenresList(callbackGenres)
    }

    // возвращает liveData для подписки на нее
    // инициирует запросы на сервер
    fun getData(moviesListType: String): MutableLiveData<AppState> {
        getMoviesListFromRemoteSource(moviesListType)
        return liveData
    }

    // запросы на сервер
    private fun getMoviesListFromRemoteSource(moviesListType: String) {
        liveData.value = AppState.Loading
        retrofitRepository.apply {
            getMoviesList(moviesListType, callbackMoviesList)
        }
    }

    // обработка ответа с сервера на запрос списка фильмов
    private val callbackMoviesList = object : Callback<TmdbResponse.ResponseMoviesList> {
        override fun onResponse(
            call: Call<TmdbResponse.ResponseMoviesList>,
            responseMoviesList: Response<TmdbResponse.ResponseMoviesList>
        ) {
            val responseBody = responseMoviesList.body()
            if (responseMoviesList.isSuccessful) {
                if (responseBody != null) {
                    liveData.postValue(checkResponse(responseBody.movieFromLists))
                } else {
                    liveData.postValue(AppState.Error(RESPONSE_MOVIES_LIST_ERROR))
                    Log.d(TAG, RESPONSE_MOVIES_LIST_ERROR)
                }
            }
        }

        override fun onFailure(call: Call<TmdbResponse.ResponseMoviesList>, t: Throwable) {
            liveData.postValue(AppState.Error(RESPONSE_MOVIES_LIST_ERROR))
            Log.e(TAG, RESPONSE_MOVIES_LIST_ERROR, t)
        }
    }

    // обработка ответа с сервера на запрос фильма по id
    private val callbackMovieById = object : Callback<TmdbMovieByIdDTO> {
        override fun onResponse(
            call: Call<TmdbMovieByIdDTO>,
            responseMovie: Response<TmdbMovieByIdDTO>
        ) {
            if (responseMovie.isSuccessful) {
                val responseBody = responseMovie.body()
                if (responseBody != null) {
                    Log.d(TAG, "Movie: $responseBody")
                } else {
                    Log.d(TAG, RESPONSE_MOVIE_BY_ID_ERROR)
                }
            }
        }

        override fun onFailure(call: Call<TmdbMovieByIdDTO>, t: Throwable) {
            Log.e(TAG, RESPONSE_MOVIE_BY_ID_ERROR, t)
        }
    }

    private fun checkResponse(serverResponse: List<TmdbMovieFromListDTO>): AppState {
        convertDtoToModel(serverResponse)
        return if (serverResponse.isNullOrEmpty()) {
            AppState.Error(CORRUPTED_DATA)
        } else {
            AppState.Success(moviesMap)

        }
    }

    private fun convertDtoToModel(serverResponse: List<TmdbMovieFromListDTO>) {
        serverResponse.forEach { movieDTO ->
            val movie = Movie(
                movieDTO.id.toString(),
                movieDTO.title,
                movieDTO.releaseDate,
                movieDTO.voteAverage.toString(),
                genresMap[movieDTO.genreIds[0]], // фильм может иметь несколько категорий жанров, отображается первый в списке жанр
                movieDTO.overview,
                movieDTO.posterPath,
                movieDTO.backdropPath
            )
            // Список жанров формируется в виде genresSet и берется из приодящего списка фильмов.
            // Таким образом в адаптер для фильтрации по жанрам передаются только те жанры, которые
            // имеются в полученном списке фильмов.
            // Это исключает появление в адаптере пустых отфильстрованных по жанрам списков
            moviesMap[movie.id.toString()] = movie
        }
    }
}