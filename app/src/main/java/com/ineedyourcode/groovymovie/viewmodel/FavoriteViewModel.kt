package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.db.entities.FavoriteEntity

class FavoriteViewModel: ViewModel() {
    private val roomFavoriteRepository: IRoomRepository = RoomRepository(App.getMovieDao())

    fun getAllFavorite() = roomFavoriteRepository.getAllFavorite()

    fun saveFavorite(entity: FavoriteEntity){
        roomFavoriteRepository.saveFavoriteEntity(entity)
    }

    fun deleteFavorite(entity: FavoriteEntity){
        roomFavoriteRepository.deleteFavorite(entity.movieId)
    }

    fun deleteAllFavorite() {
        roomFavoriteRepository.clearAllFavorite()
    }
}