package com.ineedyourcode.groovymovie.viewmodel

import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.App
import com.ineedyourcode.groovymovie.model.db.IRoomRepository
import com.ineedyourcode.groovymovie.model.db.RoomRepository
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO

class NoteViewModel: ViewModel() {

    private val roomNoteRepository: IRoomRepository =
        RoomRepository(App.getMovieDao())

    fun saveNote(movie: TmdbMovieByIdDTO, noteContent: String) {
        roomNoteRepository.saveNote(movie, noteContent)
    }

    fun getNote(movie: TmdbMovieByIdDTO): NotesEntity = roomNoteRepository.getNote(movie.id)

    fun deleteNote(movie: TmdbMovieByIdDTO){
        roomNoteRepository.deleteNote(movie.id)
    }
}