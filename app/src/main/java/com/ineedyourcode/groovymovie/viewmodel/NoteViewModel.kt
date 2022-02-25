package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDto

class NoteViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val roomNoteRepository: IRoomRepository = RoomRepository(App.getMovieDao())
) : ViewModel() {


    fun getNote(movie: TmdbMovieByIdDto): MutableLiveData<AppState> {
        getNoteResponse(movie)
        return liveData
    }

    fun saveNote(movie: TmdbMovieByIdDto, noteContent: String) {
        roomNoteRepository.saveNote(movie, noteContent)
    }

    fun deleteNote(movie: TmdbMovieByIdDto) {
        roomNoteRepository.deleteNote(movie.id)
    }

    private fun getNoteResponse(movie: TmdbMovieByIdDto) {
        var note = roomNoteRepository.getNote(movie.id)
        if (note == null) {
            saveNote(movie, "")
            note = roomNoteRepository.getNote(movie.id)
        }
        liveData.postValue(AppState.NoteSuccess(note!!))
    }
}