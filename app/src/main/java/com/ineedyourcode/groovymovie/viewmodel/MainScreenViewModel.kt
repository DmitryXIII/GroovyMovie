package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainScreenViewModel(private val liveDataToObserve: MutableLiveData<Any> = MutableLiveData()) :
    ViewModel() {

    fun getData(): LiveData<Any> {
        getDataFromDb()
        return liveDataToObserve
    }

    private fun getDataFromDb() {
        Thread {
            sleep(5000)
            liveDataToObserve.postValue(Any())
        }.start()
    }
}