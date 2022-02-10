package com.ineedyourcode.groovymovie.view

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomappbar.BottomAppBar
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentMainBinding
import com.ineedyourcode.groovymovie.view.contacts.ContactsFragment
import com.ineedyourcode.groovymovie.view.history.HistoryFragment
import com.ineedyourcode.groovymovie.view.settings.SettingsFragment
import com.ineedyourcode.groovymovie.view.tabs.TabFragment
import kotlinx.android.synthetic.main.fragment_main.*

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

        binding.menuNavDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    if (parentFragmentManager.findFragmentByTag(SettingsFragment().TAG_FOR_BACKSTACK) == null) {
                        menuAction(SettingsFragment(), SettingsFragment().TAG_FOR_BACKSTACK)
                    } else {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    true
                }
                R.id.action_contacts -> {
                    if (parentFragmentManager.findFragmentByTag(ContactsFragment().TAG_FOR_BACKSTACK) == null) {
                        menuAction(ContactsFragment(), ContactsFragment().TAG_FOR_BACKSTACK)
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

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.tab_fragment_container, TabFragment())
            .commit()

        // инициализация Navdrawer и его привязка к bottomBar
        binding.drawerLayout.addDrawerListener(ActionBarDrawerToggle(
            activity,
            binding.drawerLayout,
            binding.bottomBar,
            R.string.appbar_scrolling_view_behavior,
            R.string.appbar_scrolling_view_behavior
        ).also { it.syncState() })

        // цвет navigation icon
        binding.bottomBar.navigationIcon!!.setColorFilter(
            resources.getColor(R.color.neutral_white, context?.theme),
            PorterDuff.Mode.SRC_ATOP
        )

        // цвет иконки внутри FAB
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

    private fun menuAction(fragment: Fragment, tag: String) {
        parentFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .add(R.id.fragment_container, fragment, tag)
            .addToBackStack("")
            .commit()

        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
