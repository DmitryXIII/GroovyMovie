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
import com.ineedyourcode.groovymovie.utils.showSnackWithoutAction
import com.ineedyourcode.groovymovie.view.contacts.ContactsFragment
import com.ineedyourcode.groovymovie.view.history.HistoryFragment
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
                    if (parentFragmentManager.findFragmentByTag(SettingsFragment().TAG_FOR_BACKSTACK) == null) {
                        menuNavDrawerAction(
                            SettingsFragment(),
                            SettingsFragment().TAG_FOR_BACKSTACK
                        )
                    } else {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                R.id.action_contacts -> {
                    if (parentFragmentManager.findFragmentByTag(ContactsFragment().TAG_FOR_BACKSTACK) == null) {
                        menuNavDrawerAction(
                            ContactsFragment(),
                            ContactsFragment().TAG_FOR_BACKSTACK
                        )
                    } else {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                else -> {
                    false
                }
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
                    if (parentFragmentManager.findFragmentByTag(TabFragment().TAG_FOR_BACKSTACK) == null) {
                        menuBottomNavAction(
                            TabFragment(),
                            TabFragment().TAG_FOR_BACKSTACK
                        )
                    }
                    true
                }
                R.id.action_history -> {
                    if (parentFragmentManager.findFragmentByTag(HistoryFragment().TAG_FOR_BACKSTACK) == null) {
                        menuBottomNavAction(
                            HistoryFragment(),
                            HistoryFragment().TAG_FOR_BACKSTACK
                        )
                    }
                    true
                }
                R.id.action_favorite -> {
                    binding.navigationContainer.showSnackWithoutAction("Избранное")
                    true
                }
                else -> false
            }
        }
    }

    private fun menuNavDrawerAction(fragment: Fragment, tag: String) {
        parentFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(R.id.main_fragment_container, fragment, tag)
            .addToBackStack("")
            .commit()

        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun menuBottomNavAction(fragment: Fragment, tag: String) {
        parentFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.navigation_container, fragment, tag)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
