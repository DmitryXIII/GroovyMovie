package com.ineedyourcode.groovymovie.view.tabs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ineedyourcode.groovymovie.view.MainScreenFragment
import com.ineedyourcode.groovymovie.view.MovieDetailsFragment


class PagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm, lc)  {
    override fun getItemCount(): Int {
        return 6
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> MainScreenFragment()
            1 -> MainScreenFragment()
            2 -> MainScreenFragment()
            3 -> MainScreenFragment()
            4 -> MainScreenFragment()
            5 -> MainScreenFragment()
            else -> Fragment()
        }
    }
}