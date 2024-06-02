package com.skku.fixskkufront

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        hideSystemUI()

        // 인텐트를 처리합니다.


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNav)
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
                    val chatFragment = ChatFragment()
                    openFragment(chatFragment)
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

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val action: String? = intent.action
        val data: Uri? = intent.data

        if (Intent.ACTION_VIEW == action && data != null && data.path == "/seat") {

            val campusType = data.getQueryParameter("campusType")?.toIntOrNull() ?: 0
            val buildingName = data.getQueryParameter("buildingName") ?: ""
            val classRoomName = data.getQueryParameter("classRoomName") ?: ""
            val selectedSeatIndex = data.getQueryParameter("selectedSeatIndex")?.toIntOrNull() ?: -1
            Log.d("Int", data.toString())
            val fragment = SeatFragment().apply {
                arguments = Bundle().apply {
                    putInt("campusType", campusType)
                    putString("building_name", buildingName)
                    putString("classRoomName", classRoomName)
                    putInt("selected_seat_index", selectedSeatIndex)
                }
            }
            openFragment(fragment)
        }
        else if (action == "시설물 조회") {
            val fragment = CurrentFragment()
            openFragment(fragment)
        } else if (action == "신고 요청") {
            val fragment = SeatFragment()
            openFragment(fragment)
        } else if (action == "신고 조회") {
            val fragment = MyReportFragment()
            openFragment(fragment)
        }
        else {
            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNav)
            bottomNavigationView.selectedItemId = R.id.home
        }
    }

    private fun openFragment(fragment: Fragment) {
        Log.d("wrong", fragment.toString())
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
