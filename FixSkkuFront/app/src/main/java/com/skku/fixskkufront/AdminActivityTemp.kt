package com.skku.fixskkufront

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import java.util.Locale
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.io.InputStreamReader

/* 테스트를 위한 AdminActivity 백업본입니다! */
class AdminActivityTemp : AppCompatActivity() {

    companion object{
        var items = ArrayList<AdminRoom>()
        var item_init = ArrayList<AdminRoom>()
    }
    var isFilterApplied = false
    var isPressedAnyButton = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
        val jsonString = """{
            "code": 200,
            "message": "신고 조회 성공",
            "data": {
                "page": 1,
                "totalPages": 41,
                "reports": [
                    {
                        "reportId": 1,
                        "facilityId": "",
                        "token": "",
                        "description": "의자 고장",
                        "photoUrl": "",
                        "reportStatus": "수리 접수",
                        "creationDate": "24-05-06 8:24 p.m.",
                        "rejectionReason": "",
                        "campus": "",
                        "building": "제1공학관",
                        "floor": "5층",
                        "classroom": "34호(21534) A-28",
                        "facilityType": "CHAIR",
                        "facilityStatus": ""
                    },
                    {
                        "reportId": 2,
                        "facilityId": "",
                        "token": "",
                        "description": "",
                        "photoUrl": "",
                        "reportStatus": "반려",
                        "creationDate": "24-05-06 7:15 p.m.",
                        "rejectionReason": "",
                        "campus": "",
                        "building": "제2공학관",
                        "floor": "1층",
                        "classroom": "24호(22124) A-13",
                        "facilityType": "TABLE",
                        "facilityStatus": ""
                    }
                ]
            }
        }"""

        fetchAndParseJson(jsonString)

        val myAdapter = AdminRoomAdapter(items, this)
        val listView = findViewById<ListView>(R.id.listViewChatRoom)
        listView.adapter = myAdapter

        val searchView = findViewById<SearchView>(R.id.editTextSearch)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(myAdapter ,newText)
                return true
            }
        })

        val btnSort = findViewById<Button>(R.id.sort_button)
        var SortToggle = true
        btnSort.setOnClickListener {
            if (SortToggle){
                items.sortBy { it.time } // 오름차순
                SortToggle = false
            }
            else{
                items.sortByDescending { it.time } // 내림차순
                SortToggle = true
            }
            myAdapter.updateList(items)
        }
        val btnBefore = findViewById<Button>(R.id.button2)
        val btnIng = findViewById<Button>(R.id.button3)
        val btnAfter = findViewById<Button>(R.id.button4)
        val btnReject = findViewById<Button>(R.id.button5)

        var btnBeforePressed = false
        var btnIngPressed = false
        var btnAfterPressed = false
        var btnRejectPressed = false

        btnBefore.setOnClickListener {
            items = item_init
            btnIngPressed = false
            btnAfterPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnBeforePressed) {
                toggleFilter()
                applyFilter(myAdapter, "수리 접수")
            }

            toggleFilter()
            applyFilter(myAdapter, "수리 접수")

            val currentTextColor = btnBefore.currentTextColor

            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true
                btnBeforePressed = true

                btnBefore.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnBefore.setTextColor(Color.WHITE)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }

            else {
                isPressedAnyButton = false
                btnBeforePressed = false
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
            }
        }

        btnIng.setOnClickListener {
            items = item_init
            btnBeforePressed = false
            btnAfterPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnIngPressed) {
                toggleFilter()
                applyFilter(myAdapter, "수리 중")
            }

            toggleFilter()
            applyFilter(myAdapter, "수리 중")
            val currentTextColor = btnIng.currentTextColor

            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true
                btnIngPressed = true

                btnIng.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnIng.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }

            else {
                isPressedAnyButton = false
                btnIngPressed = false
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
            }
        }

        btnAfter.setOnClickListener {
            items = item_init
            btnBeforePressed = false
            btnIngPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnAfterPressed) {
                toggleFilter()
                applyFilter(myAdapter, "수리 완료")
            }

            toggleFilter() //
            applyFilter(myAdapter, "수리 완료")
            val currentTextColor = btnAfter.currentTextColor

            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true
                btnAfterPressed = true

                btnAfter.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnAfter.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }

            else {
                isPressedAnyButton = false
                btnAfterPressed = false
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
            }
        }

        btnReject.setOnClickListener {
            items = item_init
            btnBeforePressed = false
            btnIngPressed = false
            btnAfterPressed = false

            if(isPressedAnyButton && !btnRejectPressed) {
                toggleFilter()
                applyFilter(myAdapter, "반려")
            }
            toggleFilter()
            applyFilter(myAdapter, "반려")
            val currentTextColor = btnReject.currentTextColor

            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true
                btnRejectPressed = true

                btnReject.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnReject.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
            }

            else {
                isPressedAnyButton = false
                btnRejectPressed = false
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun search(adapter: AdminRoomAdapter, newText: String?) {
        if( newText != null){
            val searchedItems = items.filter { it.name.toLowerCase().contains(newText.toLowerCase())
                    || it.text.toLowerCase().contains(newText.toLowerCase())
                    || it.status.toLowerCase().contains(newText.toLowerCase())
                    || it.time.toLowerCase().contains(newText.toLowerCase())  }
            adapter.updateList(searchedItems) // 필터된 목록으로 어댑터 업데이트
        }
        else {
            adapter.updateList(items) // 전체 목록으로 어댑터 업데이트
        }
    }
    private fun toggleFilter() {
        isFilterApplied = !isFilterApplied
    }

    private fun applyFilter(adapter: AdminRoomAdapter, state: String) {
        if (isFilterApplied) {
            val filteredItems = items.filter { it.status == state }
            adapter.updateList(filteredItems)
        } else { adapter.updateList(items) }
    }
    private fun fetchAndParseJson(url: String) {
        try {

            val gson = Gson()
            val reportResponse = gson.fromJson(url, ReportResponse::class.java)
            val reports = reportResponse.data.reports

            items.clear()
            item_init.clear()

            for (report in reports) {
                val iconResId = when (report.facilityType.toUpperCase(Locale.ROOT)) {
                    "CHAIR" -> R.drawable.baseline_chair_24
                    "TABLE" -> R.drawable.baseline_table_restaurant_24
                    "ELECTRICAL" -> R.drawable.baseline_electrical_services_24
                    else -> R.drawable.baseline_sort_24
                }

                val adminRoom = AdminRoom(
                    report.facilityType + "고장",
                    "${report.building} ${report.floor} ${report.classroom}",
                    iconResId,
                    report.reportStatus,
                    report.creationDate,
                    report.description
                )

                items.add(adminRoom)
                item_init.add(adminRoom)
            }

        } catch (e: IOException) { e.printStackTrace() }
    }

}
