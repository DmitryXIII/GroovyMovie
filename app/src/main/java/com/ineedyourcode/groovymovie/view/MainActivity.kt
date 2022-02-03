package com.ineedyourcode.groovymovie.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.utils.*
import com.ineedyourcode.groovymovie.view.tabs.PagerAdapter

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager_2)
        val viewPagerAdapter = PagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
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
}
