package com.ineedyourcode.groovymovie.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainBinding
import com.ineedyourcode.groovymovie.view.contacts.ContactsFragment
import com.ineedyourcode.groovymovie.view.favorite.FavoriteFragment
import com.ineedyourcode.groovymovie.view.history.HistoryFragment
import com.ineedyourcode.groovymovie.view.maps.MapsFragment
import com.ineedyourcode.groovymovie.view.settings.SettingsFragment
import com.ineedyourcode.groovymovie.view.tabs.TabFragment

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                R.id.action_contacts -> {
                    menuNavDrawerAction(ContactsFragment(), ContactsFragment().TAG_FOR_BACKSTACK)
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
                R.id.action_maps -> {
                    menuBottomNavAction(MapsFragment(), MapsFragment().TAG_FOR_BACKSTACK)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
