package com.ineedyourcode.groovymovie.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.android.material.snackbar.Snackbar
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainScreenBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.MainScreenViewModel

class MainScreenFragment : Fragment() {

    private lateinit var adapterComedy: MovieListAdapter
    private lateinit var adapterDrama: MovieListAdapter
    private lateinit var adapterTriller: MovieListAdapter
    private lateinit var adapterFamily: MovieListAdapter
    private lateinit var adapterAction: MovieListAdapter

    private lateinit var moviesList: List<Movie>
    private lateinit var moviesListComedy: List<Movie>
    private lateinit var moviesListDrama: List<Movie>
    private lateinit var moviesListTriller: List<Movie>
    private lateinit var moviesListFamily: List<Movie>
    private lateinit var moviesListAction: List<Movie>

    private lateinit var recyclerViewAction: RecyclerView
    private lateinit var recyclerViewComedy: RecyclerView
    private lateinit var recyclerViewDrama: RecyclerView
    private lateinit var recyclerViewTriller: RecyclerView
    private lateinit var recyclerViewFamily: RecyclerView

    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: NestedScrollView
    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer<Any> {
            renderData(it as AppState)
        })

        adapterComedy = MovieListAdapter()
        adapterDrama = MovieListAdapter()
        adapterTriller = MovieListAdapter()
        adapterFamily = MovieListAdapter()
        adapterAction = MovieListAdapter()

        val searchLayout = binding.tfInputSearch
        val searchValue = binding.tfEditSearch

        progressBar = binding.spinKit
        progressBar.indeterminateDrawable = ThreeBounce()

        scrollView = binding.scroll

        recyclerViewAction = binding.moviesRecyclerviewAction
        recyclerViewComedy = binding.moviesRecyclerviewComedy
        recyclerViewDrama = binding.moviesRecyclerviewDrama
        recyclerViewTriller = binding.moviesRecyclerviewTriller
        recyclerViewFamily = binding.moviesRecyclerviewFamily

        recyclerViewAction.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewComedy.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDrama.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTriller.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFamily.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewAction.adapter = adapterAction
        recyclerViewComedy.adapter = adapterComedy
        recyclerViewDrama.adapter = adapterDrama
        recyclerViewTriller.adapter = adapterTriller
        recyclerViewFamily.adapter = adapterFamily

        adapterAction.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                onMovieClick(moviesListAction, position)
            }
        })

        adapterComedy.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                onMovieClick(moviesListComedy, position)
            }
        })

        adapterDrama.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                onMovieClick(moviesListDrama, position)
            }
        })

        adapterTriller.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                onMovieClick(moviesListTriller, position)
            }
        })

        adapterFamily.setOnItemClickListener(object : MovieListAdapter.OnItemClickListener {
            override fun onItemClickListener(position: Int) {
                onMovieClick(moviesListFamily, position)
            }
        })

        searchLayout.setEndIconOnClickListener(View.OnClickListener {
            if ((searchValue.text.toString().isBlank())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_request),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Поиск \"${searchValue.text}\"",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            val imm: InputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        })
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                progressBar.isVisible = false
                scrollView.visibility = View.VISIBLE

                moviesList = appState.moviesData
                filterByGenres(moviesList)

            }
            is AppState.Loading -> {
                progressBar.isVisible = true
                scrollView.visibility = View.INVISIBLE
            }
            is AppState.Error -> {
                progressBar.isVisible = false
                scrollView.visibility = View.INVISIBLE
                Snackbar
                    .make(binding.tfInputSearch, appState.error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry)) { viewModel.getData() }
                    .show()
            }
        }
    }

    private fun filterByGenres(moviesList: List<Movie>) {
        moviesListComedy = moviesList.filter { it.genre == "Комедия" }
        moviesListAction = moviesList.filter { it.genre == "Боевик" }
        moviesListTriller = moviesList.filter { it.genre == "Триллер" }
        moviesListDrama = moviesList.filter { it.genre == "Драма" }
        moviesListFamily = moviesList.filter { it.genre == "Семейный" }

        adapterAction.setAdapterData(moviesListAction)
        adapterComedy.setAdapterData(moviesListComedy)
        adapterDrama.setAdapterData(moviesListDrama)
        adapterTriller.setAdapterData(moviesListTriller)
        adapterFamily.setAdapterData(moviesListFamily)
    }

    private fun onMovieClick(moviesList: List<Movie>, position: Int) {
        val selectedMovie = moviesList[position]
        parentFragmentManager.beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment_container, MovieInfoFragment.newInstance(selectedMovie))
            .addToBackStack("")
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}