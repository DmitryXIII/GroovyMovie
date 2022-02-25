package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository

class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val roomHistoryRepository: IRoomRepository = RoomRepository(App.getMovieDao())
) : ViewModel() {

    fun getHistory(): MutableLiveData<AppState> {
        getHistoryResponse()
        return liveData
    }

    private fun getHistoryResponse() {
        val history = roomHistoryRepository.getAllHistory()
        liveData.postValue(AppState.HistorySuccess(history))
    }

    fun clearHistory() = roomHistoryRepository.clearAllHistory()
}