package com.ineedyourcode.groovymovie.view.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentNoteBinding
import com.ineedyourcode.groovymovie.model.db.entities.NotesEntity
import com.ineedyourcode.groovymovie.model.tmdb.dto.TmdbMovieByIdDTO
import com.ineedyourcode.groovymovie.utils.hideKeyboard
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.NoteViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var note: NotesEntity

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private lateinit var selectedMovie: TmdbMovieByIdDTO

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: TmdbMovieByIdDTO) = NoteFragment().apply {
            arguments = bundleOf(
                ARG_MOVIE to movie
            )
            selectedMovie = movie
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteMovieTitle.text = getString(R.string.note_content_title, selectedMovie.title)

        noteViewModel.getNote(selectedMovie).observe(viewLifecycleOwner, Observer<Any> {
            when (it) {
                is AppState.NoteSuccess -> {
                    note = it.note
                    binding.noteContent.setText(note.noteContent)
                }
            }
        })

        binding.fabSaveNote.setOnClickListener {
            view.hideKeyboard()
            noteViewModel.saveNote(selectedMovie, binding.noteContent.text.toString())
            view.showSnackWithoutAction("Заметка сохранена")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // если содержание заметки при выходе пустое, то заметка удаляется из БД
        if (binding.noteContent.text.isNullOrBlank()) {
            noteViewModel.deleteNote(selectedMovie)
        }

        _binding = null
    }
}