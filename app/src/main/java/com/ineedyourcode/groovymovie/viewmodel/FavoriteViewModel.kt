package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity

class FavoriteViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val roomFavoriteRepository: IRoomRepository = RoomRepository(App.getMovieDao())
) : ViewModel() {

    fun getAllFavorite(): MutableLiveData<AppState> {
        getAllFavoriteResponse()
        return liveData
    }

    fun saveFavorite(entity: FavoriteEntity) {
        roomFavoriteRepository.saveFavoriteEntity(entity)
    }

    fun deleteFavorite(entity: FavoriteEntity) {
        roomFavoriteRepository.deleteFavorite(entity.movieId)
    }

    fun deleteAllFavorite() {
        roomFavoriteRepository.clearAllFavorite()
    }

    private fun getAllFavoriteResponse() {
        val favoriteList = roomFavoriteRepository.getAllFavorite()
        liveData.postValue(AppState.FavoriteListSuccess(favoriteList))
    }
}