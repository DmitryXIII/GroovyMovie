package com.ineedyourcode.groovymovie.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbActorDto
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val CORRUPTED_DATA = "Неполные данные"
private const val RESPONSE_MOVIE_BY_ID_ERROR = "Response movie by id failed"
private const val RESPONSE_ACTOR_BY_ID_ERROR = "Response actor by id failed"
private const val TAG = "MOVIE_DETAILS_VIEW_MODEL"

class MovieDetailsViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: IRetrofitRepository = RetrofitRepository(RemoteDataSource())
) : ViewModel() {

    // возвращает liveData для подписки на нее
    // инициирует запросы на сервер
    fun getMovieById(movieId: Int): MutableLiveData<AppState> {
        getMovieByIdFromRemoteSource(movieId)
        return liveData
    }

    // запросы на сервер
    private fun getMovieByIdFromRemoteSource(movieId: Int) {
        liveData.value = AppState.Loading
        retrofitRepository.getMovieByIdWithCredits(movieId, callbackMovieById)
    }

    // возвращает liveData для подписки на нее
    // инициирует запросы на сервер
    fun getActorById(actorId: Int): MutableLiveData<AppState> {
        getActorsByIdFromRemoteSource(actorId)
        return liveData
    }

    // запросы на сервер
    private fun getActorsByIdFromRemoteSource(actorId: Int) {
//        liveData.value = AppState.Loading
        retrofitRepository.getActorById(actorId, callbackActorById)
    }

    // обработка ответа с сервера на запрос фильма по id
    private val callbackMovieById = object : Callback<TmdbMovieByIdDTO> {
        override fun onResponse(
            call: Call<TmdbMovieByIdDTO>,
            responseMovie: Response<TmdbMovieByIdDTO>
        ) {
            if (responseMovie.isSuccessful) {
                if (responseMovie.body() != null) {

                    // отправка данных о фильме в фрагмент
                    liveData.postValue(AppState.MovieByIdSuccess(responseMovie.body()!!))
                    // загрузка актеров по пришедшему в фильме списку id актеров
                    responseMovie.body()!!.credit.cast.forEach {
                        getActorById(it.id)
                    }
                    Log.d(TAG, "Movie: ${responseMovie.body()}")
                } else {
                    liveData.postValue(AppState.Error(RESPONSE_MOVIE_BY_ID_ERROR))
                    Log.d(TAG, RESPONSE_MOVIE_BY_ID_ERROR)
                }
            }
        }

        override fun onFailure(call: Call<TmdbMovieByIdDTO>, t: Throwable) {
            Log.e(TAG, RESPONSE_MOVIE_BY_ID_ERROR, t)
        }
    }

    // обработка ответа с сервера на запрос фильма по id
    private val callbackActorById = object : Callback<TmdbActorDto> {
        override fun onResponse(
            call: Call<TmdbActorDto>,
            responseActor: Response<TmdbActorDto>
        ) {
            if (responseActor.isSuccessful) {
                val actorFromRemoteServer = responseActor.body()
                if (responseActor.body() != null) {
                    // отправка данных об актере в фрагмент
                    liveData.postValue(AppState.ActorsByIdSuccess(responseActor.body()!!))
                    Log.d(TAG, "Actor: $actorFromRemoteServer")
                } else {
                    liveData.postValue(AppState.Error(RESPONSE_MOVIE_BY_ID_ERROR))
                    Log.d(TAG, RESPONSE_MOVIE_BY_ID_ERROR)
                }
            }
        }

        override fun onFailure(call: Call<TmdbActorDto>, t: Throwable) {
            Log.e(TAG, RESPONSE_ACTOR_BY_ID_ERROR, t)
        }
    }
}