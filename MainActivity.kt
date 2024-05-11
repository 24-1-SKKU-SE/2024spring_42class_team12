package com.skku.fixskkufront

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val items = ArrayList<AdminRoom>()
        /* ?덉떆 */
        items.add(AdminRoom("?섏옄 怨좎옣", "??怨듯븰愿 5痢?34??21534)\nA-28", R.drawable.ic_launcher_background, "?섎━ ?묒닔", "24-05-06 8:24 p.m."))
        items.add(AdminRoom("梨낆긽 怨좎옣", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad.", R.drawable.ic_launcher_background, "諛섎젮", "24-05-06 7:15 p.m."))
        items.add(AdminRoom("肄섏꽱??怨좎옣", "Hey.", R.drawable.ic_launcher_background, "?섎━ 以?, "24-05-06 4:21 p.m."))
        items.add(AdminRoom("?섏옄 怨좎옣", "Emoji", R.drawable.ic_launcher_background, "?섎━ ?꾨즺", "24-05-06 4:05 p.m."))
        items.add(AdminRoom("?섏옄 怨좎옣", "??怨듯븰愿 5痢?34??21534)\nA-28", R.drawable.ic_launcher_background, "?섎━ ?묒닔", "24-05-06 8:24 p.m."))
        items.add(AdminRoom("梨낆긽 怨좎옣", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad.", R.drawable.ic_launcher_background, "諛섎젮", "24-05-06 7:15 p.m."))
        items.add(AdminRoom("肄섏꽱??怨좎옣", "Hey.", R.drawable.ic_launcher_background, "?섎━ 以?, "24-05-06 4:21 p.m."))
        items.add(AdminRoom("?섏옄 怨좎옣", "Emoji", R.drawable.ic_launcher_background, "?섎━ ?꾨즺", "24-05-06 4:05 p.m."))

        val myAdapter = AdminRoomAdapter(items, applicationContext)
        val listView = findViewById<ListView>(R.id.listViewChatRoom)
        listView.adapter = myAdapter


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
