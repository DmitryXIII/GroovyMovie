package com.ineedyourcode.groovymovie.view.history

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentHistoryBinding
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.view.BaseBindingFragment
import com.ineedyourcode.groovymovie.viewmodel.AppState
import com.ineedyourcode.groovymovie.viewmodel.HistoryViewModel

class HistoryFragment :
    BaseBindingFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    val TAG_FOR_BACKSTACK = "HistoryFragment"

    private lateinit var historyAdapter: HistoryAdapter

    private val viewModelHistory: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelHistory.getHistory().observe(viewLifecycleOwner, Observer<Any> {
            when (it) {
                is AppState.HistorySuccess -> {
                    historyAdapter = HistoryAdapter()
                    historyAdapter.setAdapterData(it.history)
                    binding.historyRecyclerview.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = historyAdapter
                    }
                }
            }
        })

        // Замена цвета иконки внутри FAB
        ImageViewCompat.setImageTintList(
            binding.fabClearHistory,
            ColorStateList.valueOf(resources.getColor(R.color.main_background, context?.theme))
        )

        binding.fabClearHistory.setOnClickListener {
            viewModelHistory.clearHistory()
            historyAdapter.clearData()
            view.showSnackWithoutAction("HISTORY CLEARED")
        }
    }
}