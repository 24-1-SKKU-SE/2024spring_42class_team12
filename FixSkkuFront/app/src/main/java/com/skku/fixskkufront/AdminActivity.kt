package com.skku.fixskkufront

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.Collections
import java.util.Locale
import android.widget.Filter;
import android.widget.Filterable;

class AdminActivity : AppCompatActivity() {

    companion object{
        var items = ArrayList<AdminRoom>()
        var item_init = ArrayList<AdminRoom>()
    }
    var isFilterApplied = false // 필터 적용 여부를 추적하기 위한 변수
    var isPressedAnyButton = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        fetchAndParseJson("http://13.124.89.169:8081/?reportStatus=fixed&startDate=&endDate=&searchWord=")

        val myAdapter = AdminRoomAdapter(items, this)
        val listView = findViewById<ListView>(R.id.listViewChatRoom)
        listView.adapter = myAdapter

        /* SearchView 로 검색 기능 구현 */
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
        /* 시간 순서대로 리스트뷰 정렬 기능 구현 */
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
        /* 상태 필터 버튼*/
        val btnInit = findViewById<Button>(R.id.button6)
        val btnBefore = findViewById<Button>(R.id.button2)
        val btnIng = findViewById<Button>(R.id.button3)
        val btnAfter = findViewById<Button>(R.id.button4)
        val btnReject = findViewById<Button>(R.id.button5)

        /* 수리 상태에 따른 필터 버튼 */
        var btnInitPressed = false
        var btnBeforePressed = false
        var btnIngPressed = false
        var btnAfterPressed = false
        var btnRejectPressed = false

        btnInit.setOnClickListener {
            items = item_init

            btnBeforePressed = false
            btnIngPressed = false
            btnAfterPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnInitPressed) {
                toggleFilter()
                applyFilter(myAdapter, "신고 접수")
            }

            toggleFilter()
            applyFilter(myAdapter, "신고 접수")

            val currentTextColor = btnInit.currentTextColor
            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true
                btnInitPressed = true

                btnInit.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnInit.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }
            else {
                isPressedAnyButton = false
                btnInitPressed = false
                btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
                btnInit.setTextColor(Color.BLACK)
            }
        }

        /* 수리 접수 버튼을 눌렀을 때, 나머지 검정색(활성화된) 버튼을 하얀색(비활성화)으로 */
        btnBefore.setOnClickListener {
            /* 원본 대입. clear하면 원본 리스트도 왜인지 모르게 사라져서 대안으로 작성된 코드 */
            items = item_init

            btnInitPressed = false
            btnIngPressed = false
            btnAfterPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnBeforePressed) {
                // 다른 버튼이 눌린 상태에서(필터가 돼 있는 상황에서), 이 버튼을 누른다면 (자기 자신을 누른다면은 빼야함)
                toggleFilter() // 필터 상태 전환
                applyFilter(myAdapter, "수리 접수") // 필터 적용
            }

            toggleFilter() // 필터 상태 전환
            applyFilter(myAdapter, "수리 접수") // 필터 적용

            val currentTextColor = btnBefore.currentTextColor
            /* 텍스트 컬러가 검은색(활성화)이면 버튼을 눌렀을 때 하얀색(필터 해제 = 전체 보기)으로. 나머지 버튼 배경 모두 하얀색 */
            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true // 버튼이 눌린 상태
                btnBeforePressed = true // 자기 자신이 눌린 상태

                btnBefore.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnBefore.setTextColor(Color.WHITE)
                btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
                btnInit.setTextColor(Color.BLACK)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }
            /* 텍스트 컬러가 하얀색(비활성화)이면, 버튼을 눌렀을 때 검은색(수리 접수 필터)으로. */
            else {
                isPressedAnyButton = false // 버튼이 안 눌린 상태
                btnBeforePressed = false
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
            }
        }

        btnIng.setOnClickListener {
            items = item_init
            btnInitPressed = false
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
                btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
                btnInit.setTextColor(Color.BLACK)
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
            btnInitPressed = false
            btnBeforePressed = false
            btnIngPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnAfterPressed) {
                toggleFilter()
                applyFilter(myAdapter, "수리 완료")
            }

            toggleFilter()
            applyFilter(myAdapter, "수리 완료")
            val currentTextColor = btnAfter.currentTextColor

            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true
                btnAfterPressed = true

                btnAfter.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnAfter.setTextColor(Color.WHITE)
                btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
                btnInit.setTextColor(Color.BLACK)
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
            btnInitPressed = false
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
                btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
                btnInit.setTextColor(Color.BLACK)
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
            val searchedItems = AdminActivityTemp.items.filter { it.name.toLowerCase().contains(newText.toLowerCase())
                    || it.text.toLowerCase().contains(newText.toLowerCase())
                    || it.status.toLowerCase().contains(newText.toLowerCase())
                    || it.time.toLowerCase().contains(newText.toLowerCase())  }
            adapter.updateList(searchedItems) // 필터된 목록으로 어댑터 업데이트
        }
        else {
            adapter.updateList(AdminActivityTemp.items) // 전체 목록으로 어댑터 업데이트
        }
    }
    private fun toggleFilter() {
        isFilterApplied = !isFilterApplied // 필터 상태를 토글
    }

    private fun applyFilter(adapter: AdminRoomAdapter, state: String) {
        if (isFilterApplied) { // 필터가 적용된 경우, "현재 STATE"인 아이템만 보이도록 필터링
            val filteredItems = items.filter { it.status == state }
            adapter.updateList(filteredItems) // 필터된 목록으로 어댑터 업데이트
        } else { // 필터가 적용되지 않은 경우, 전체 아이템 표시
            adapter.updateList(items) // 전체 목록으로 어댑터 업데이트
        }
    }
    private fun fetchAndParseJson(url: String) {

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    //Toast.makeText(this@AdminActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
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
                            else -> R.drawable.baseline_sort_24 // Default icon if none match
                        }

                        val adminRoom = AdminRoom(
                            report.description,
                            "${report.building} ${report.floor} ${report.classroom}",
                            iconResId,
                            report.reportStatus,
                            report.creationDate,
                            report.description
                        )

                        items.add(adminRoom)
                        item_init.add(adminRoom)
                    }

                    runOnUiThread {
                        val myAdapter = AdminRoomAdapter(items, this@AdminActivity)
                        val listView = findViewById<ListView>(R.id.listViewChatRoom)
                        listView.adapter = myAdapter
                    }
                }
            }
        })
    }

}
