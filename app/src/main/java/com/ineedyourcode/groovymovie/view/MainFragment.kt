package com.ineedyourcode.groovymovie.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.ineedyourcode.groovymovie.BuildConfig
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainBinding
import com.ineedyourcode.groovymovie.utils.showToast
import com.ineedyourcode.groovymovie.view.favorite.FavoriteFragment
import com.ineedyourcode.groovymovie.view.history.HistoryFragment
import com.ineedyourcode.groovymovie.view.settings.SettingsFragment
import com.ineedyourcode.groovymovie.view.tabs.TabFragment

class MainFragment : BaseBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // на старте отображается основной фрагмент со списками фильмов
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.navigation_container, TabFragment())
            .commit()

        // лисенер бокового меню
        binding.menuNavDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    menuNavDrawerAction(SettingsFragment(), SettingsFragment().TAG_FOR_BACKSTACK)
                }
                R.id.action_about -> {
                    showToast(requireContext(), BuildConfig.VERSION_NAME)
                    true
                }
                else -> false
            }
        }

        // инициализация Navdrawer и его привязка к bottomBar
        binding.drawerLayout.addDrawerListener(ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            binding.toolbar,
            R.string.appbar_scrolling_view_behavior,
            R.string.appbar_scrolling_view_behavior
        ).also { it.syncState() })

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_movies_list -> {
                    menuBottomNavAction(TabFragment(), TabFragment().TAG_FOR_BACKSTACK)
                }
                R.id.action_history -> {
                    menuBottomNavAction(HistoryFragment(), HistoryFragment().TAG_FOR_BACKSTACK)
                }
                R.id.action_favorite -> {
                    menuBottomNavAction(FavoriteFragment(), FavoriteFragment().TAG_FOR_BACKSTACK)
                }
                else -> false
            }
        }
    }

    private fun menuNavDrawerAction(fragment: Fragment, tag: String): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        if (parentFragmentManager.findFragmentByTag(tag) == null) {
            parentFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.main_fragment_container, fragment, tag)
                .addToBackStack("")
                .commit()
        }
        return true
    }

    private fun menuBottomNavAction(fragment: Fragment, tag: String): Boolean {
        if (parentFragmentManager.findFragmentByTag(tag) == null) {
            parentFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.navigation_container, fragment, tag)
                .commit()
        }
        return true
    }
}
