package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.db.entities.HistoryEntity

class HistoryViewModel: ViewModel() {
    private val roomHistoryRepository: IRoomRepository = RoomRepository(App.getMovieDao())

    fun getHistory(): List<HistoryEntity> = roomHistoryRepository.getAllHistory()

    fun clearHistory() = roomHistoryRepository.clearAllHistory()
}