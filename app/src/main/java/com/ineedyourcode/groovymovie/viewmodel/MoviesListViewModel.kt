package com.ineedyourcode.groovymovie.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val CORRUPTED_DATA = "Неполные данные"
private const val RESPONSE_MOVIES_LIST_ERROR = "Failed to get response movies list"
private const val TAG = "RETROFIT_VIEW_MODEL"

class MoviesListViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: IRetrofitRepository = RetrofitRepository(RemoteDataSource()),
    private val roomHistoryRepository: IRoomRepository = RoomRepository(App.getMovieDao())
) : ViewModel() {

    fun saveHistory(movie: TmdbMovieByIdDTO) {
        roomHistoryRepository.saveHistoryEntity(movie)
    }

    // возвращает liveData для подписки на нее
    // инициирует запросы на сервер
    fun getMoviesList(moviesListType: String): MutableLiveData<AppState> {
        getMoviesListFromRemoteSource(moviesListType)
        return liveData
    }

    // запросы на сервер
    private fun getMoviesListFromRemoteSource(moviesListType: String) {
        liveData.value = AppState.Loading
        retrofitRepository.getMoviesList(moviesListType, callbackMoviesList)
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

    private fun checkResponse(moviesList: List<TmdbMovieByIdDTO>): AppState {
        return if (moviesList.isNullOrEmpty()) {
            AppState.Error(CORRUPTED_DATA)
        } else {
            AppState.MoviesListSuccess(moviesList)
        }
    }
}