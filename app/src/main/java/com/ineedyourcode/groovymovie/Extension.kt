package com.ineedyourcode.groovymovie

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.hideKeyboard(): Boolean {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

//снэкбар без action, принимает в параметрах строковый ресурс
fun View.showSnackWithoutAction(@StringRes message: Int) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(resources.getColor(R.color.orange, context.theme))
    snackbar.setTextColor(resources.getColor(R.color.main_background, context.theme))
    snackbar.show()
}

//снэкбар без action, принимает в параметрах String
fun View.showSnackWithoutAction(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(resources.getColor(R.color.orange, context.theme))
    snackbar.setTextColor(resources.getColor(R.color.main_background, context.theme))
    snackbar.show()
}

fun View.showSnackWithAction(message: String, actionText: String, action: (View) -> Unit) {
    val snackbar =
        Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).setAction(actionText, action)
    snackbar.setBackgroundTint(resources.getColor(R.color.orange, context.theme))
    snackbar.setTextColor(resources.getColor(R.color.main_background, context.theme))
    snackbar.setActionTextColor(resources.getColor(R.color.appbar_layuot, context.theme))
    snackbar.show()
}