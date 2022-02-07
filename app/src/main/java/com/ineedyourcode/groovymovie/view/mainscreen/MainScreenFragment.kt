package com.ineedyourcode.groovymovie.view.mainscreen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainScreenBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.showSnackWithAction
import com.ineedyourcode.groovymovie.utils.GridSpacingItemDecoration
import com.ineedyourcode.groovymovie.view.details.MovieDetailsFragment
import com.ineedyourcode.groovymovie.view.history.HistoryFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.ViewModelRetrofit

@RequiresApi(Build.VERSION_CODES.N)
class MainScreenFragment : Fragment() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var mainAdapter: MoviesListAdapter
    private lateinit var moviesListType: String
    private lateinit var progressBar: ProgressBar // кастомный прогрессбар

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelRetrofit by lazy {
        ViewModelProvider(this)[ViewModelRetrofit::class.java]
    }

    companion object {
        private const val ARG_MOVIE_TYPE = "ARG_MOVIE_TYPE"

        fun newInstance(moviesListType: String) =
            MainScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE_TYPE, moviesListType)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let{
            moviesListType = it.getString(ARG_MOVIE_TYPE)!!
        }

        viewModel.getData(moviesListType).observe(viewLifecycleOwner, Observer<Any> {
            renderData(it as AppState)
        })

        with(binding) {
            progressBar = spinKit
            mainRecyclerView = binding.mainRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()

        binding.fab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(
                    R.id.fragment_container,
                    HistoryFragment()
                )
                .addToBackStack("")
                .commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                progressBar.isVisible = false
                mainRecyclerView.visibility = View.VISIBLE

                mainAdapter = MoviesListAdapter()
                mainAdapter.setAdapterData(appState.moviesData)
                mainAdapter.setOnItemClickListener(object :
                    MoviesListAdapter.OnItemClickListener {
                    override fun onItemClickListener(
                        position: Int,
                        moviesList: List<Movie>
                    ) {
                        parentFragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(
                                R.id.fragment_container,
                                MovieDetailsFragment.newInstance(moviesList[position])
                            )
                            .addToBackStack("")
                            .commit()

                        viewModel.saveHistory(moviesList[position])
                    }
                })
                Log.d("MainScreen", "Movies: ${appState.moviesData}")

                mainRecyclerView.apply {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    addItemDecoration(GridSpacingItemDecoration(2, 50, true))
                    adapter = mainAdapter
                }
            }

            is AppState.Loading -> {
                progressBar.isVisible = true
                mainRecyclerView.visibility = View.INVISIBLE
            }

            is AppState.Error -> {
                progressBar.isVisible = false
                mainRecyclerView.visibility = View.INVISIBLE

                view?.showSnackWithAction(
                    appState.e,
                    getString(R.string.retry)
                ) {
                    viewModel.getData(moviesListType)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}