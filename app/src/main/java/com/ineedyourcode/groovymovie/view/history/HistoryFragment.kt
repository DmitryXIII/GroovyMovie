package com.ineedyourcode.groovymovie.view.history

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentHistoryBinding
import com.ineedyourcode.groovymovie.utils.GridSpacingItemDecoration
import com.ineedyourcode.groovymovie.utils.showSnackWithAction
import com.ineedyourcode.groovymovie.view.MoviesListAdapter
import com.ineedyourcode.groovymovie.viewmodel.mainscreen.AppState
import com.ineedyourcode.groovymovie.viewmodel.retrofit.ViewModelRetrofit

class HistoryFragment : Fragment() {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var mainAdapter: HistoryAdapter

    private lateinit var progressBar: ProgressBar // кастомный прогрессбар

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModelHistory: ViewModelRetrofit by lazy {
        ViewModelProvider(this)[ViewModelRetrofit::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressBar = spinKit
            mainRecyclerView = binding.mainRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()

        mainAdapter = HistoryAdapter()
        mainAdapter.setAdapterData(viewModelHistory.getHistory())

        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mainAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}