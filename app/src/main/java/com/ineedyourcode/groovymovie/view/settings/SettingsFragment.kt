package com.ineedyourcode.groovymovie.view.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.utils.PREFERENCES_ADULT

class SettingsFragment : Fragment() {
    val TAG_FOR_BACKSTACK = "SettingsFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkBox: CheckBox = view.findViewById(R.id.checkbox_settings_adult)
        val settings = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = settings?.edit()

        // проверяется значение FILTER ADULT в настройках,
        // если true - чекбокс при открытии фрагмента включен
        checkBox.isChecked =
            activity?.getPreferences(Context.MODE_PRIVATE)!!.getBoolean(PREFERENCES_ADULT, false)

        // запись значения FILTER ADULT в настройки
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
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