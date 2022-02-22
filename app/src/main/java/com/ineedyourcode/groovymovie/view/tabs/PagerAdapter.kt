package com.ineedyourcode.groovymovie.view.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ineedyourcode.groovymovie.utils.*
import com.ineedyourcode.groovymovie.view.movieslist.MoviesListFragment


class PagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm, lc)  {
    override fun getItemCount(): Int {
        return 6
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> MoviesListFragment.newInstance(MOVIES_LIST_TOP_RATED)
            1 -> MoviesListFragment.newInstance(MOVIES_LIST_POPULAR)
            2 -> MoviesListFragment.newInstance(MOVIES_LIST_UPCOMING)
            3 -> MoviesListFragment.newInstance(MOVIES_LIST_NOW_PLAYING)
            4 -> MoviesListFragment.newInstance(MOVIES_LIST_UPCOMING)
            5 -> MoviesListFragment.newInstance(MOVIES_LIST_TOP_RATED)
            else -> Fragment()
        }
    }
}