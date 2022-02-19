package com.ineedyourcode.groovymovie.view.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import com.ineedyourcode.groovymovie.databinding.FragmentSettingsBinding
import com.ineedyourcode.groovymovie.utils.PREFERENCES_ADULT
import com.ineedyourcode.groovymovie.view.BaseBindingFragment

class SettingsFragment :
    BaseBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    val TAG_FOR_BACKSTACK = "SettingsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = settings?.edit()

        binding.checkboxSettingsAdult.apply {
            // проверяется значение FILTER ADULT в настройках,
            // если true - чекбокс при открытии фрагмента включен
            isChecked = activity?.getPreferences(Context.MODE_PRIVATE)!!
                .getBoolean(PREFERENCES_ADULT, false)

            setOnClickListener {
                // запись значения FILTER ADULT в настройки
                if (this.isChecked) {
                    editor?.apply {
                        putBoolean(PREFERENCES_ADULT, true)
                        apply()
                    }
                } else {
                    editor?.apply {
                        putBoolean(PREFERENCES_ADULT, false)
                        apply()
                    }
                }
            }
        }
    }
}