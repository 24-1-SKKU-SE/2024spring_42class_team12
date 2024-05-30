package com.skku.fixskkufront

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
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
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userButton: Button = findViewById<Button>(R.id.userBtn)
        val adminButton: Button = findViewById<Button>(R.id.adminBtn)

        userButton.setOnClickListener {
            val intent = Intent(this@MainActivity, CampusSelectActivity::class.java)
            startActivity(intent)
        }

        adminButton.setOnClickListener {
            showAdminDialog()
        }
    }

    private fun showAdminDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Admin Code")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

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
}