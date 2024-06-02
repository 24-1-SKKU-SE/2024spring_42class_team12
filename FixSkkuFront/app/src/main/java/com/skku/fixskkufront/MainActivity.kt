package com.skku.fixskkufront

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemUI()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userButton: Button = findViewById<Button>(R.id.userBtn)
        val adminButton: Button = findViewById<Button>(R.id.adminBtn)

        userButton.setOnClickListener {
            val intent = Intent(this@MainActivity, UserActivity::class.java)
            startActivity(intent)
        }

        adminButton.setOnClickListener {
            showAdminDialog()
        }
    }

    private fun showAdminDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Admin Code")

        // Create a container to hold the EditText
        val container = FrameLayout(this)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER

        // Set layout parameters to match the width of the dialog title and buttons
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 0, 50, 0)  // Set margin to add some space
        input.layoutParams = params

        container.addView(input)
        builder.setView(container)

        builder.setPositiveButton("Enter") { dialog, _ ->
            val enteredCode = input.text.toString()
            if (enteredCode == "1234") {
                val intent = Intent(this@MainActivity, AdminActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Incorrect code", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }


    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}