package com.ineedyourcode.groovymovie.view.favorite

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentFavoriteBinding
import com.ineedyourcode.groovymovie.databinding.FragmentMoviesListBinding
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.utils.PREFERENCES_ADULT
import com.ineedyourcode.groovymovie.utils.showSnackWithAction
import com.ineedyourcode.groovymovie.view.details.MovieDetailsFragment
import com.ineedyourcode.groovymovie.view.movieslist.MoviesListAdapter
import com.ineedyourcode.groovymovie.view.movieslist.MoviesListFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel
import com.ineedyourcode.groovymovie.viewmodel.RetrofitViewModel

class FavoriteFragment : Fragment() {
    val TAG_FOR_BACKSTACK = "FavoriteFragment"
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var moviesListType: String
    private lateinit var progressBar: ProgressBar // кастомный прогрессбар

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllFavorite()

        with(binding) {
            progressBar = favoriteSpinKit
            favoriteRecyclerView = binding.favoriteRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()

        favoriteAdapter = FavoriteAdapter().also {
            it.setAdapterData(viewModel.getAllFavorite())
        }

        favoriteRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoriteAdapter
        }

        binding.fabClearFavorite.setOnClickListener {
            viewModel.deleteAllFavorite()
            favoriteAdapter.clearData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}