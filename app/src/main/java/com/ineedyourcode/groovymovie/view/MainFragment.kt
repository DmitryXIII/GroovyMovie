package com.ineedyourcode.groovymovie.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentTransaction
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainBinding
import com.ineedyourcode.groovymovie.view.history.HistoryFragment
import com.ineedyourcode.groovymovie.view.tabs.TabFragment

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.tab_fragment_container, TabFragment())
            .commit()

        // Замена цвета иконки внутри FAB
        ImageViewCompat.setImageTintList(
            binding.fab,
            ColorStateList.valueOf(resources.getColor(R.color.main_background, context?.theme))
        )

        binding.fab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(
                    R.id.fragment_container,
                    HistoryFragment()
                )
                .addToBackStack("")
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}