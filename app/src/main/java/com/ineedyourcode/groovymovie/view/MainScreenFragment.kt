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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainScreenBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.MainScreenViewModel

class MainScreenFragment : Fragment() {

    private var moviesMap: Map<String, Movie> = mapOf()
    private var genresList: List<String> = listOf()

    private lateinit var mainRecyclerView: RecyclerView // главный (вертикальный) ресайклервью с вложенными горизонтальными ресайклервьюхами
    private lateinit var mainAdapter: MainMoviesAdapter // адаптер для главного ресайклервью

    private lateinit var searchLayout: TextInputLayout
    private lateinit var searchValue: TextInputEditText
    private lateinit var progressBar: ProgressBar // кастомный прогрессбар
//    private lateinit var scrollView: NestedScrollView

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

        with(binding) {
            searchLayout = tfInputSearch
            searchValue = tfEditSearch
//            scrollView = scroll
            progressBar = spinKit
            mainRecyclerView = binding.mainRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()

        mainAdapter = MainMoviesAdapter()
        mainAdapter.setAdapterData(moviesMap, genresList, requireContext(), this)
        mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainRecyclerView.adapter = mainAdapter

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
                mainRecyclerView.visibility = View.VISIBLE
//                scrollView.visibility = View.VISIBLE

                moviesMap = appState.moviesData
                genresList = appState.genresData

                mainAdapter.setAdapterData(moviesMap, genresList, requireContext(), this)

            }
            is AppState.Loading -> {
                progressBar.isVisible = true
                mainRecyclerView.visibility = View.INVISIBLE
//                scrollView.visibility = View.INVISIBLE
            }
            is AppState.Error -> {
                progressBar.isVisible = false
                mainRecyclerView.visibility = View.INVISIBLE
//                scrollView.visibility = View.INVISIBLE
                Snackbar
                    .make(searchValue, appState.error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.retry)) { viewModel.getData() }
                    .show()
            }
        }
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