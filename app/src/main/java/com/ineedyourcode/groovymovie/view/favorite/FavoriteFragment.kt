package com.ineedyourcode.groovymovie.view.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentFavoriteBinding
import com.ineedyourcode.groovymovie.utils.GridDecorator
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {
    val TAG_FOR_BACKSTACK = "FavoriteFragment"
    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = FavoriteAdapter()

        viewModel.getAllFavorite().observe(viewLifecycleOwner, Observer<Any> {
            when (it) {
                is AppState.FavoriteListSuccess -> {
                    favoriteAdapter.setAdapterData(it.favoriteList)
                }
            }
        })

        with(binding) {
            progressBar = favoriteSpinKit
            favoriteRecyclerView = binding.favoriteRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()

        favoriteRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoriteAdapter
            addItemDecoration(
                GridDecorator(
                    2,
                    resources.getDimensionPixelSize(R.dimen.movie_item_width).toFloat()
                )
            )
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