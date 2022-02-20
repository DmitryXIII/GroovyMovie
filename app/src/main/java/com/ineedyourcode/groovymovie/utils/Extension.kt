package com.ineedyourcode.groovymovie.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ineedyourcode.groovymovie.R

fun View.hideKeyboard(): Boolean {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

//снэкбар без action, принимает в параметрах строковый ресурс
fun View.showSnackWithoutAction(@StringRes message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(resources.getColor(R.color.secondary_background, context.theme))
        setTextColor(resources.getColor(R.color.main_background, context.theme))
        show()
    }
}

//снэкбар без action, принимает в параметрах String
fun View.showSnackWithoutAction(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(resources.getColor(R.color.secondary_background, context.theme))
        setTextColor(resources.getColor(R.color.main_background, context.theme))
        show()
    }
}

//снэкбар без action, принимает в параметрах action
fun View.showSnackWithAction(message: String, actionText: String, action: (View) -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).setAction(actionText, action).apply {
        setBackgroundTint(resources.getColor(R.color.secondary_background, context.theme))
        setTextColor(resources.getColor(R.color.main_background, context.theme))
        setActionTextColor(resources.getColor(R.color.action_color, context.theme))
        show()
    }
}

// Методы используются для корректной загрузки и отображения backdrop-фона фильма.
// Backdrop корректно растягивается на ширину дисплея без обрезки
// ни по вертикали, ни по горизонтали.
// ratio - отношение ширины к высоте backdrop-фона, загружаемого из TMDB (= 1,77777...)
fun Fragment.getImageWidth(): Int {
    val displayMetrics = DisplayMetrics()
    activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Fragment.getImageHeight(ratio: Double): Int {
    val displayMetrics = DisplayMetrics()
    activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    return (displayMetrics.widthPixels / ratio).toInt()
}

fun CardView.setBackgroundColorByRating(rating: Double) {
    when (rating) {
        in 0.0..4.0 -> {
            setCardBackgroundColor(context.getColor(R.color.color_until_3_9))
        }
        in 4.1..6.9 -> {
            setCardBackgroundColor(context.getColor(R.color.color_until_6_9))
        }
        else -> {
            setCardBackgroundColor(context.getColor(R.color.color_until_9_9))
        }
    }
}
