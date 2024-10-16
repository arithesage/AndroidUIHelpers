@file:Suppress("unused")

package me.arithesage.kotlin.android.helpers.ui.prefabs

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout


class LoginUI (appContext: Context?)
    : UIPrefab<LinearLayout> (appContext) {
    private lateinit var _usernameField: EditText
    private lateinit var _passwordField: PasswordField

    override fun setup(appContext: Context) {
        ui = LinearLayout (appContext)

        _usernameField = EditText (appContext)
        _usernameField.hint = "Username"
        _usernameField.id = (0..Int.MAX_VALUE).random()
        _usernameField.maxLines = 1

        _passwordField = PasswordField (appContext)

        ui.addView (_usernameField)
        ui.addView (_passwordField.ui())

        val usernameFieldLayoutParams = _usernameField.layoutParams
        usernameFieldLayoutParams.width =
            LinearLayout.LayoutParams.MATCH_PARENT
        _usernameField.layoutParams = usernameFieldLayoutParams

        _passwordField.setupLayoutFor (ui)
    }


    fun onAccept () {
        _passwordField.onAccept()
    }


    fun password (): String {
        return _passwordField.password()
    }


    fun passwordField (): EditText {
        return _passwordField.passwordField()
    }


    fun username (): String {
        return _usernameField.text.toString()
    }


    fun usernameField (): EditText {
        return _usernameField
    }
}
