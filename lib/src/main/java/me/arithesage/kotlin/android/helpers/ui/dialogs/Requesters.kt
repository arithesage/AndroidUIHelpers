@file:Suppress (
    "FunctionName",
    "MemberVisibilityCanBePrivate",
    "ObjectLiteralToLambda",
    "RedundantExplicitType",
    "RedundantSamConstructor",
    "RemoveEmptyParenthesesFromLambdaCall",
    "SENSELESS_COMPARISON",
    "UNUSED",
    "UNUSED_ANONYMOUS_PARAMETER",
    "UNUSED_PARAMETER",
    "VARIABLE_WITH_REDUNDANT_INITIALIZER"
)

package me.arithesage.kotlin.android.helpers.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import me.arithesage.kotlin.android.helpers.ui.Utils
import me.arithesage.kotlin.android.helpers.ui.prefabs.LoginUI

/**
 * The Requesters object is meant to show more or less complex dialogs that
 * requests an information from the user, like a username or an integer value.
 */
object Requesters {
    private var appContext: Context? = null

    fun Init (appContext: Context) {
        if (appContext != null) {
            this.appContext = appContext
        }
    }


    fun Initialized () : Boolean {
        return (this.appContext != null)
    }


    fun Login (requesterTitle: String?,
               requestCaption: String,
               onAccept: (Map<String, String>) -> Unit)
    {
        val formUI = LoginUI (appContext)

        val formLayout = HashMap<Int, String> ()
        formLayout.put (formUI.usernameField().id, "username")
        formLayout.put (formUI.passwordField().id, "password")

        Request (
            requesterTitle,
            requestCaption,
            onAccept = {
                loginData: Map<String, String> ->
                formUI.onAccept()
                onAccept (loginData)
            },
            formUI.ui(),
            formLayout,
            null
        )
    }


    /**
     * Requests an integer value.
     *
     * Is the same as requesting an string and converting it to an
     * integer but using an input field that only accepts numbers
     * (opens the numeric keyboard, not the full one).
     */
    fun RequestInt (requesterTitle: String?,
                    requestCaption: String,
                    onAccept: (Int) -> Unit)
    {
        val inputField: EditText = EditText (appContext)
        inputField.inputType = InputType.TYPE_CLASS_NUMBER

        RequestData (
            requesterTitle,
            requestCaption,
            onAccept = {
                response: String ->
                try {
                    onAccept (response.toInt())
                } catch (ex: NumberFormatException) {
                    onAccept (-1)
                }
            },
            inputField,
            null
        )
    }


    fun RequestString (requesterTitle: String?,
                       requestCaption: String,
                       onAccept: (String) -> Unit)
    {
        RequestData (
            requesterTitle,
            requestCaption,
            onAccept = { response: String ->
                onAccept (response)
            },
            null,
            null)
    }


    /**
     * Shows a dialog for requesting some data.
     *
     * By default, the dialog contains an input field that accepts
     * an string, but you can provide a custom input field, along with
     * an optional set of layout params if you wish.
     *
     * You can specify a custom input field, a custom set of layout
     * params for your input field (or the default one), or both.
     *
     * @param requesterTitle The title the dialog will show
     * @param requestCaption Info about the data being requested
     * @param onAccept A lambda to process the response if accepted
     * @param customInputField An optional custom input field to use
     * @param customInputFieldLayout An optional set of layout params to use
     */
    fun RequestData (requesterTitle: String?,
                     requestCaption: String,
                     onAccept: (String) -> Unit,
                     customInputField: EditText?,
                     customInputFieldLayout: LayoutParams?)
    {
        if (!Initialized()) {
            return
        }

        // If object was initialized, we have a valid Application Context
        val appContext: Context = (this.appContext as Context)

        // Starting dialog creation
        val request = AlertDialog.Builder (appContext)

        // We want to ensure that our UI will have a
        // ConstraintLayout as parent
        val uiContainer = ConstraintLayout (appContext)

        // Here is we start creating our dialog's UI

        // We want to show only an input field, so we are going
        // to use a LinearLayout with VERTICAL orientation to hold it.
        //
        // This is a very simple layout:
        // Each view will be placed below the previous one.
        val ui = LinearLayout (appContext)
        ui.orientation = LinearLayout.VERTICAL

        var inputField: EditText? = null

        // If a custom input field is provided, use it
        if (customInputField != null) {
            inputField = customInputField

        } else {
            // Here is our input field. Will only accept one line of text.
            inputField = EditText(appContext)
            inputField.maxLines = 1
        }

        // Now we add the field to the UI
        ui.addView (inputField)

        // Next, we add the UI to the container
        uiContainer.addView (ui)

        // And finally, we set the container as the view being
        // showed by the requester
        request.setView (uiContainer)

        // So, we have an input field parented to a linear layout with
        // vertical orientation (so, if we add more views, they will be placed
        // one below another) and the linear view is parented to a
        // constraint layout.

        // Now, after adding views to their parents, we can tweak their
        // layout params if needed. You cannot adjust a view's layout params
        // before placing it in a container.

        // To do that, we first obtain a copy of the view's layout params.
        // Note that a view's layout params object is null before being
        // parented to a container.
        val uiLayout = ui.layoutParams as ConstraintLayout.LayoutParams

        // Next, we adjust the layout params as needed.
        //
        // In this case, we want our linear layout, that holds our
        // input field, to fill the container where is placed, so,
        // we set its width and height params to match their parent ones.
        //
        // Also, we are adding a little margin to each side.
        uiLayout.width = LayoutParams.MATCH_PARENT
        uiLayout.height = LayoutParams.MATCH_PARENT
        uiLayout.leftMargin = 50
        uiLayout.rightMargin = 50

        // And finally, we apply the modified layout to the view
        ui.layoutParams = uiLayout

        // If no custom layout is provided for the input field, create the
        // default one

        var inputFieldLayout: LayoutParams? = null

        if (customInputFieldLayout != null) {
            inputFieldLayout = customInputFieldLayout

        } else {
            inputFieldLayout = inputField.layoutParams as LayoutParams
            inputFieldLayout.width = LayoutParams.MATCH_PARENT
        }

        inputField.layoutParams = inputFieldLayout

        // Now, let's complete our dialog.
        // If a title was given, set it.
        if (requesterTitle != null) {
            request.setTitle (requesterTitle)
        }

        // Then we set the caption.
        // That is, what the requester is requesting.
        request.setMessage (requestCaption)

        // And finally, define how the requester's button will behave.
        // Here we are enabling two buttons.
        //
        // The positive one is the one located to the right and
        // will be our 'Accept' button.
        //
        // The negative button is the one located to the left and
        // will be our 'Cancel' button.
        //
        // We can also add a neutral button, that will be located
        // at the center.
        request.setPositiveButton (
            "Accept",
            object: DialogInterface.OnClickListener {
                override fun onClick (dialog: DialogInterface,
                                      whichButton: Int)
                {
                    // In this case, of a requester for strings,
                    // we pass to the function a lambda that accepts an string
                    // parameter, so we can return our response.

                    onAccept (inputField.text.toString())
                }
            }
        )

        request.setNegativeButton (
            "Cancel",
            DialogInterface.OnClickListener() {
                dialog: DialogInterface,
                whichButton: Int ->

                fun onClick (dialog: DialogInterface,
                             whichButton: Int) {

                }
            }
        )

        // Now is time for creating and showing our dialog
        request.create().show ()
    }


    /**
     * This requester allows a lot of flexibility.
     *
     * Is intended for requesting a bunch of data, so, you need to provide
     * the entire form UI as a LinearLayout that will be oriented vertically.
     *
     * You can also provide a set of layout params for this UI, only keep in
     * mind that the parent is a ConstraintLayout.
     *
     * One of the requested params is a 'formLayout' (note that this isn't
     * a LayoutParams object).
     *
     * Due the requester will show a entirely custom UI, that can have
     * many different fields, in order to return the entered data we use a
     * dictionary.
     *
     * The formLayout is used to know which field data goes to which key.
     * So, let's suppose you have a login form with a username and a
     * password input fields. Each one have a integer id.
     *
     * Now, we create a formLayout object where we put two entries:
     * (usernameField.id, "username") and (passwordField.id, "password").
     *
     * Now, when accept button is clicked, the requester iterates the
     * formLayout dictionary, searches for the fields by their id and
     * in the returned dictionary it puts the entered data.
     *
     * @param requesterTitle The dialog title
     * @param requestCaption Info about the requested data
     *
     * @param onAccept A lambda that returns a dictionary with
     * the entered data in the form 'field = value'
     *
     * @param formUI The form UI
     *
     * @param formLayout A dictionary containing the form fields's ids
     * and the name of the dictionary key where the input data will be
     * stored
     *
     * @param formUILayoutParams Optional layout params for the formUI
     */
    fun Request (requesterTitle: String?,
                 requestCaption: String,
                 onAccept: (HashMap<String, String>) -> Unit,
                 formUI: LinearLayout,
                 formLayout: Map<Int, String>,
                 formUILayoutParams: LayoutParams?)
    {
        if (!Initialized()) {
            return
        }

        val appContext: Context = (this.appContext as Context)

        val request = AlertDialog.Builder (appContext)

        val uiContainer = ConstraintLayout (appContext)

        formUI.orientation = LinearLayout.VERTICAL

        uiContainer.addView (formUI)

        request.setView (uiContainer)

        if (formUILayoutParams != null) {
            formUI.layoutParams = formUILayoutParams

        } else {
            val formUILayout =
                formUI.layoutParams as ConstraintLayout.LayoutParams

            formUILayout.width = LayoutParams.MATCH_PARENT
            formUILayout.height = LayoutParams.MATCH_PARENT
            formUILayout.leftMargin = 50
            formUILayout.rightMargin = 50

            formUI.layoutParams = formUILayout
        }

        if (requesterTitle != null) {
            request.setTitle (requesterTitle)
        }

        request.setMessage (requestCaption)

        request.setPositiveButton (
            "Accept",
            object: DialogInterface.OnClickListener {
                override fun onClick (dialog: DialogInterface,
                                      whichButton: Int)
                {
                    val inputData = HashMap <String, String>()

                    formLayout.forEach {
                        entry ->

                        val field = formUI.findViewById<EditText>(entry.key)
                        inputData.put (entry.value, field.text.toString())
                    }

                    onAccept (inputData)
                }
            }
        )

        request.setNegativeButton (
            "Cancel",
            DialogInterface.OnClickListener() {
                    dialog: DialogInterface,
                    whichButton: Int ->

                fun onClick (dialog: DialogInterface,
                             whichButton: Int)
                {

                }
            }
        )

        request.create().show ()
    }
}




















