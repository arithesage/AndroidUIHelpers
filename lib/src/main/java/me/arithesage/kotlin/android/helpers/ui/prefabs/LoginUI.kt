@file:Suppress("FunctionName", "unused" , "UNUSED_ANONYMOUS_PARAMETER")

package me.arithesage.kotlin.android.helpers.ui.prefabs

import android.content.Context
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import me.arithesage.kotlin.helpers.utils.Hashing


class LoginUI (appContext: Context?) {
    private lateinit var ui: LinearLayout
    //private lateinit var passwordFieldContainer: TextInputLayout

    private lateinit var _usernameField: EditText
    //private lateinit var passwordField: EditText
    private lateinit var _passwordField: PasswordField

    init {
        if (appContext != null) {
            ui = LinearLayout(appContext)

            // We use a TextInputLayout to wrap the passwordField
            // because doing things this way will automatically add a
            // 'eye' button to allow seeing the entered password.
            //passwordFieldContainer = TextInputLayout (appContext)

            _usernameField = EditText (appContext)
            _usernameField.hint = "Username"
            _usernameField.id = (0..Int.MAX_VALUE).random()
            _usernameField.maxLines = 1

            _passwordField = PasswordField (appContext)
            /*
            passwordField = EditText (appContext)
            passwordField.hint = "Password"
            passwordField.id = (0..Int.MAX_VALUE).random()
            passwordField.maxLines = 1

            passwordField.inputType = (TYPE_CLASS_TEXT or 
                                       TYPE_TEXT_VARIATION_PASSWORD)

            passwordFieldContainer.addView (passwordField)
            passwordFieldContainer.endIconMode = 
                TextInputLayout.END_ICON_PASSWORD_TOGGLE
            */

            ui.addView (_usernameField)
            ui.addView (_passwordField.ui())
            //ui.addView (passwordFieldContainer)

            // Adjust both password field container and username field to
            // fill the ui width.

            /*
            val passwordContainerParams = passwordFieldContainer.layoutParams
            passwordContainerParams.width = 
                LinearLayout.LayoutParams.MATCH_PARENT
            passwordFieldContainer.layoutParams = passwordContainerParams
            */

            val passwordFieldParams = _passwordField.ui().layoutParams
            passwordFieldParams.width =
                    LinearLayout.LayoutParams.MATCH_PARENT
            _passwordField.ui().layoutParams = passwordFieldParams


            val usernameFieldLayoutParams = _usernameField.layoutParams
            usernameFieldLayoutParams.width = 
                LinearLayout.LayoutParams.MATCH_PARENT
            _usernameField.layoutParams = usernameFieldLayoutParams
        }
    }


    /**
     * Returns the entered password hashed in SHA256
     */
    /*
    fun generatePasswordHash (): String {
        val enteredPassword: String = passwordField.text.toString ()
        val hashedPassword: String = Hashing.SHA256 (enteredPassword)

        return hashedPassword
    }
    */


    fun onAccept () {
        _passwordField.onAccept()
    }

    /*
    fun onAccept () {
        if (!passwordField.text.isEmpty()) {
            val hashedPassword: String = generatePasswordHash()
            passwordField.setText (hashedPassword)
        }
    }
    */

    fun password (): String {
        return _passwordField.password()
    }

    fun passwordField (): EditText {
        return _passwordField.passwordField()
    }

    /*
    fun password (): String {
        return passwordField.text.toString ()
    }

    fun passwordField (): EditText {
        return passwordField
    }
    */

    /**
     * Returns the built UI.
     * Use this to integrate it in an Activity or another View.
     */
    fun ui () : LinearLayout {
        return ui
    }


    fun username (): String {
        return _usernameField.text.toString()
    }


    fun usernameField (): EditText {
        return _usernameField
    }
}

