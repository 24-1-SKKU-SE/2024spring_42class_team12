package com.skku.fixskkufront

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        hideSystemUI()
        val bottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val reportFragment = ReportFragment()
                    openFragment(reportFragment)
                    true
                }
                R.id.current -> {
                    val currentFragment = CurrentFragment()
                    openFragment(currentFragment)
                    true
                }
                R.id.chat -> {
//                    val chatFragment = ChatFragment()
//                    openFragment(chatFragment)
                    val intent = Intent(this@UserActivity, ChatbotActivity::class.java)
                    startActivity(intent)
                    true

                }
                R.id.myReport -> {
                    val myReportFragment = MyReportFragment()
                    openFragment(myReportFragment)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.home


    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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