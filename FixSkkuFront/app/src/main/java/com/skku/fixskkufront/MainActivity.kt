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
        /* 임시2 */
        items.add(AdminRoom("의자 고장", "제1공학관 5층 34호(21534) A-28", R.drawable.baseline_chair_24, "수리 접수", "24-05-06 8:24 p.m.", "제1공학관 5층 34호(21534) A-28 입니다!"))
        items.add(AdminRoom("책상 고장", "제2공학관 1층 24호(22124) A-13", R.drawable.baseline_table_restaurant_24, "반려", "24-05-06 7:15 p.m.", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad."))
        items.add(AdminRoom("콘센트 고장", "제2공학관 1층 24호(22124) A-15", R.drawable.baseline_electrical_services_24, "수리 중", "24-05-06 4:21 p.m.", "수리 필요. 의자."))
        items.add(AdminRoom("의자 고장", "제3공학관 2층 12호(23212) A-5", R.drawable.baseline_chair_24, "수리 완료", "24-05-06 4:05 p.m.", "공백"))
        items.add(AdminRoom("의자 고장", "제1공학관 5층 34호(21534) A-28", R.drawable.baseline_chair_24, "수리 접수", "24-05-06 8:24 p.m.", "제1공학관 5층 34호(21534) A-28 입니다!"))
        items.add(AdminRoom("책상 고장", "제2공학관 1층 24호(22124) A-13", R.drawable.baseline_table_restaurant_24, "반려", "24-05-06 7:15 p.m.", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad."))
        items.add(AdminRoom("콘센트 고장", "제2공학관 1층 24호(22124) A-15", R.drawable.baseline_electrical_services_24, "수리 중", "24-05-06 4:21 p.m.", "수리 필요. 의자."))
        items.add(AdminRoom("의자 고장", "제3공학관 2층 12호(23212) A-5", R.drawable.baseline_chair_24, "수리 완료", "24-05-06 4:05 p.m.", "공백"))

        val myAdapter = AdminRoomAdapter(items, this)
        val listView = findViewById<ListView>(R.id.listViewChatRoom)
        listView.adapter = myAdapter


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
