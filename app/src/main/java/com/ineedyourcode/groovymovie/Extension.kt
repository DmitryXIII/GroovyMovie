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
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(resources.getColor(R.color.appbar_layuot, context.theme))
        setTextColor(resources.getColor(R.color.white, context.theme))
        show()
    }
}

//снэкбар без action, принимает в параметрах String
fun View.showSnackWithoutAction(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(resources.getColor(R.color.appbar_layuot, context.theme))
        setTextColor(resources.getColor(R.color.white, context.theme))
        show()
    }
}

fun View.showSnackWithAction(message: String, actionText: String, action: (View) -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).setAction(actionText, action).apply {
        setBackgroundTint(resources.getColor(R.color.appbar_layuot, context.theme))
        setTextColor(resources.getColor(R.color.white, context.theme))
        setActionTextColor(resources.getColor(R.color.orange, context.theme))
        show()
    }
}