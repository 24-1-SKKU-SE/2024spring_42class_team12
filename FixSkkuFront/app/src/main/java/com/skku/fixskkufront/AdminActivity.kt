package com.skku.fixskkufront

import android.graphics.Color
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {

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

        //fetchAndParseJson("http://13.124.89.169/?reportStatus=fixed&startDate=&endDate=&searchWord=")
        fetchAndParseJson("http://10.0.2.2:8000/chatbot_test_get")
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
                items = item_init
                search(myAdapter ,newText)
                return true
            }
        })
        /* 시간 순서대로 리스트뷰 정렬 기능 구현 */
        val btnSort = findViewById<Button>(R.id.sort_button)
        var SortToggle = true
        btnSort.setOnClickListener {
            if (SortToggle){
                items.sortBy { it.time }
                item_init.sortBy { it.time }
                SortToggle = false
            }
            else{
                items.sortByDescending { it.time }
                item_init.sortByDescending { it.time }
                SortToggle = true
            }
            myAdapter.updateList(items)
        }

        val btnInit = findViewById<Button>(R.id.button6)
        val btnBefore = findViewById<Button>(R.id.button2)
        val btnIng = findViewById<Button>(R.id.button3)
        val btnAfter = findViewById<Button>(R.id.button4)
        val btnReject = findViewById<Button>(R.id.button5)

        var btnInitPressed = false
        var btnBeforePressed = false
        var btnIngPressed = false
        var btnAfterPressed = false
        var btnRejectPressed = false

        /* 새로고침(초기화) 버튼 */
        val btnNew = findViewById<ImageButton>(R.id.new_btn)
        btnNew.setOnClickListener {
            myAdapter.updateList(item_init)
            //fetchAndParseJson("http://13.124.89.169/?reportStatus=fixed&startDate=&endDate=&searchWord=")
            fetchAndParseJson("http://10.0.2.2:8000/chatbot_test_get")
            btnInitPressed = false
            btnBeforePressed = false
            btnIngPressed = false
            btnAfterPressed = false
            btnRejectPressed = false
            btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
            btnInit.setTextColor(Color.BLACK)
            btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
            btnBefore.setTextColor(Color.BLACK)
            btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
            btnIng.setTextColor(Color.BLACK)
            btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
            btnAfter.setTextColor(Color.BLACK)
            btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
            btnReject.setTextColor(Color.BLACK)
        }

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

        btnBefore.setOnClickListener {
            items = item_init

            btnInitPressed = false
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
                btnInit.setBackgroundResource(R.drawable.rounded_corner_white)
                btnInit.setTextColor(Color.BLACK)
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
            val searchedItems = items.filter { it.name.toLowerCase().contains(newText.toLowerCase())
                    || it.text.toLowerCase().contains(newText.toLowerCase())
                    || it.status.toLowerCase().contains(newText.toLowerCase())
                    || it.time.toLowerCase().contains(newText.toLowerCase())  }
            items = searchedItems as ArrayList<AdminRoom>
            adapter.updateList(items)
        }
        else { adapter.updateList(item_init) }
    }

    private fun toggleFilter() {
        isFilterApplied = !isFilterApplied
    }

    private fun applyFilter(adapter: AdminRoomAdapter, state: String) {
        if (isFilterApplied) {
            val filteredItems = items.filter { it.status == state }
            items = filteredItems as ArrayList<AdminRoom>
            adapter.updateList(items)
        } else { adapter.updateList(item_init) }
    }

    private fun fetchAndParseJson(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use{
                    val gson = Gson()
                    val str = response.body!!.string()
                    val reportResponse = gson.fromJson(str, ReportResponse::class.java)
                    val reports = reportResponse.data?.reports ?: emptyList()

                    items.clear()
                    item_init.clear()

                    for (report in reports) {
                        val iconResId = when (report.facilityType?.toUpperCase(Locale.ROOT)) {
                            "의자" -> R.drawable.baseline_chair_24
                            "테이블" -> R.drawable.baseline_table_restaurant_24
                            "콘센트" -> R.drawable.baseline_electrical_services_24
                            "에어컨" -> R.drawable.baseline_air_24
                            else -> R.drawable.baseline_question_mark_24 // Default icon if none match
                        }

                        val facilityDescription = "${report.building ?: ""} ${report.floor ?: ""}${report.classroom ?: ""}"
                        val adminRoom = AdminRoom(
                            "${report.facilityType ?: ""}${report.facilityStatus ?: ""}",
                            facilityDescription,
                            iconResId,
                            report.reportStatus ?: "",
                            report.creationDate ?: "",
                            report.description ?: "",
                            report.photoUrl ?: ""
                        )

                        items.add(adminRoom)
                    }

                    item_init = items
                }

            }
        })

    }

}
