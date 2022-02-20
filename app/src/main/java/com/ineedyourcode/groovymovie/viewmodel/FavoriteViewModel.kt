package com.ineedyourcode.groovymovie.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.IRetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RemoteDataSource
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RetrofitRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val RESPONSE_MOVIE_BY_ID_ERROR = "Response movie by id failed"

class FavoriteViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: IRetrofitRepository = RetrofitRepository(RemoteDataSource()),
    private val roomFavoriteRepository: IRoomRepository = RoomRepository(App.getMovieDao())
) : ViewModel() {

    fun getAllFavorite(): MutableLiveData<AppState> {
        getAllFavoriteResponse()
        return liveData
    }

    fun deleteAllFavorite() {
        roomFavoriteRepository.clearAllFavorite()
    }

    private fun checkIsFavoriteResponse(movieId: Int) {
        val isFavorite = roomFavoriteRepository.checkIsFavorite(movieId)
        liveData.postValue(AppState.IsFavoriteSuccess(isFavorite))
    }

    private fun getAllFavoriteResponse() {
        liveData.postValue(AppState.Loading)
        val favoriteListFromDB = roomFavoriteRepository.getAllFavorite()
        val favoriteMoviesList = mutableListOf<TmdbMovieByIdDto>()

        if (favoriteListFromDB.size == 0) {
            liveData.postValue(
                AppState.FavoriteListSuccess(
                    favoriteMoviesList
                )
            )
        } else {

            favoriteListFromDB.forEach {
                retrofitRepository.getMovieByIdWithCredits(
                    it.movieId,
                    object : Callback<TmdbMovieByIdDto> {
                        override fun onResponse(
                            call: Call<TmdbMovieByIdDto>,
                            responseMovie: Response<TmdbMovieByIdDto>
                        ) {
                            if (responseMovie.isSuccessful) {
                                if (responseMovie.body() != null) {
                                    favoriteMoviesList.add(responseMovie.body()!!)
                                    if (favoriteMoviesList.size == favoriteListFromDB.size) {
                                        liveData.postValue(
                                            AppState.FavoriteListSuccess(
                                                favoriteMoviesList
                                            )
                                        )
                                    }
                                } else {
                                    liveData.postValue(AppState.Error(RESPONSE_MOVIE_BY_ID_ERROR))
                                }
                            }
                        }

                        override fun onFailure(call: Call<TmdbMovieByIdDto>, t: Throwable) {
                            Log.e("FavoriteList", RESPONSE_MOVIE_BY_ID_ERROR, t)
                        }
                    })
            }
        }
    }
}