package com.ineedyourcode.groovymovie.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainScreenBinding
import com.ineedyourcode.groovymovie.hideKeyboard
import com.ineedyourcode.groovymovie.showSnackWithAction
import com.ineedyourcode.groovymovie.showSnackWithoutAction
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.MainScreenViewModel

class MainScreenFragment : Fragment() {

    private lateinit var mainRecyclerView: RecyclerView // главный (вертикальный) ресайклервью с вложенными горизонтальными ресайклервьюхами
    private lateinit var mainAdapter: MainMoviesAdapter // адаптер для главного ресайклервью

    private lateinit var searchLayout: TextInputLayout
    private lateinit var searchValue: TextInputEditText
    private lateinit var progressBar: ProgressBar // кастомный прогрессбар

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainScreenViewModel by lazy {
        ViewModelProvider(this)[MainScreenViewModel::class.java]
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
        viewModel.getData().observe(viewLifecycleOwner, Observer<Any> {
            renderData(it as AppState)
        })

        with(binding) {
            searchLayout = tfInputSearch
            searchValue = tfEditSearch
            progressBar = spinKit
            mainRecyclerView = binding.mainRecyclerview
        }

        progressBar.indeterminateDrawable = ThreeBounce()

        searchLayout.setEndIconOnClickListener(View.OnClickListener {
            if ((searchValue.text.toString().isBlank())) {
                searchLayout.showSnackWithoutAction(R.string.empty_request)
            } else {
                searchLayout.showSnackWithoutAction("Поиск \"${searchValue.text}\"")
            }
            searchLayout.hideKeyboard()
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                progressBar.isVisible = false
                mainRecyclerView.visibility = View.VISIBLE

                // Последним аргументом в адаптер передается ссылка на фрагмент,
                // чтобы можно было обработать клик по вложенным recyclerview,
                // и из главного recyclerview открыть второй экран с подробным описанием фильма.
                // Решение, как напрямую из этого фрагмента правильно обработать клик и открыть второй экран,
                // пока не найдено
                mainAdapter = MainMoviesAdapter(appState.moviesData, appState.genresData, this)

                mainRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
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

                searchValue.showSnackWithAction(
                    appState.e,
                    getString(R.string.retry)
                ) { viewModel.getData() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}