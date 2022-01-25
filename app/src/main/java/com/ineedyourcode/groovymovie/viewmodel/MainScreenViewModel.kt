package com.ineedyourcode.groovymovie.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.IMoviesRepository
import com.ineedyourcode.groovymovie.model.tmdb.TMDBRepository

@RequiresApi(Build.VERSION_CODES.N)
class MainScreenViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    private val repository: IMoviesRepository = TMDBRepository()
    private var requestsTriesCounter = 0
    fun getData(): LiveData<AppState> {
        getDataFromDb()
        return liveDataToObserve
    }

    private fun getDataFromDb() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            while (true) {
                try {
                    repository.loadData()
                    liveDataToObserve.postValue(
                        AppState.Success(
                            repository.getMoviesMap(),
                            repository.getGenresList()
                        )
                    )
                    requestsTriesCounter = 0
                    break
                } catch (e: Throwable) {
                    requestsTriesCounter++
                    if (requestsTriesCounter == 2) {
                        liveDataToObserve.postValue(AppState.Error(e.localizedMessage!!))
                        requestsTriesCounter = 0
                        break
                    }
                }
            }
        }.start()
    }
}