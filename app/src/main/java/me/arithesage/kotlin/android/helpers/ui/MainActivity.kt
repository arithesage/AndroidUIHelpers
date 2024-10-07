package me.arithesage.kotlin.android.helpers.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import me.arithesage.kotlin.android.helpers.ui.dialogs.Requesters
import me.arithesage.kotlin.android.helpers.ui.dialogs.SimpleDialogs


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SimpleDialogs.Init (this)
        Requesters.Init (this)

        Requesters.RequestInt(
            "IntTest",
            "Enter a number:",
            onAccept = { response: Int ->
                SimpleDialogs.ShowMessage (response.toString())
            }
        )

        /*
        Requesters.RequestString (
            "Test",
            "Enter data:",
            onAccept = { response: String ->
                SimpleDialogs.ShowMessage (response)
            }
        )
        */
    }
}
