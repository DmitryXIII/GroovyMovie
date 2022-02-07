package com.ineedyourcode.groovymovie.view.note

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMovieDetailsBinding
import com.ineedyourcode.groovymovie.databinding.FragmentNoteBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.viewmodel.NoteViewModel
import com.ineedyourcode.groovymovie.viewmodel.ViewModelRetrofit
import java.lang.Exception

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private lateinit var selectedMovie: Movie

    companion object {
        private const val ARG_MOVIE = "ARG_MOVIE"
        fun newInstance(movie: Movie) = NoteFragment().apply {
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

        binding.noteMovieTitle.text = selectedMovie.title

        try{
            binding.noteContent.setText(noteViewModel.getNote(selectedMovie).noteContent)
        } catch (e: Exception) {
            noteViewModel.saveNote(selectedMovie, "")
            binding.noteContent.setText("")
        }

        binding.fabSaveNote.setOnClickListener {
            noteViewModel.saveNote(selectedMovie, binding.noteContent.text.toString())
            binding.noteContent.setText(noteViewModel.getNote(selectedMovie).noteContent)
        }
    }
}