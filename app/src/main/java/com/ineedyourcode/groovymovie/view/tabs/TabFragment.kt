package com.ineedyourcode.groovymovie.view.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentNoteBinding
import com.ineedyourcode.groovymovie.databinding.FragmentTabBinding
import com.ineedyourcode.groovymovie.view.tabs.PagerAdapter

class TabFragment : Fragment() {

    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = PagerAdapter(parentFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Top rated"
                1 -> tab.text = "Popular"
                2 -> tab.text = "Upcoming"
                3 -> tab.text = "Now playing"
                4 -> tab.text = "Upcoming"
                5 -> tab.text = "Top rated"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}