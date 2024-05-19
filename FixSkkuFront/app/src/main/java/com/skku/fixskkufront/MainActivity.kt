package com.skku.fixskkufront

import android.graphics.Color
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale


class MainActivity : AppCompatActivity() {

    companion object{
        var items = ArrayList<AdminRoom>()
        var item_init = ArrayList<AdminRoom>()
    }
    var isFilterApplied = false // 필터 적용 여부를 추적하기 위한 변수
    var isPressedAnyButton = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        /* 복사본 생성 */
        addDataToList()
        addDataToList2()

        val myAdapter = AdminRoomAdapter(items, this)
        val listView = findViewById<ListView>(R.id.listViewChatRoom)
        listView.adapter = myAdapter

        /* SearchView 대신 EditText로 검색 기능 구현 */

        val btnBefore = findViewById<Button>(R.id.button2)
        val btnIng = findViewById<Button>(R.id.button3)
        val btnAfter = findViewById<Button>(R.id.button4)
        val btnReject = findViewById<Button>(R.id.button5)

        /* 수리 상태에 따른 필터 버튼 */
        var btnBeforePressed = false
        var btnIngPressed = false
        var btnAfterPressed = false
        var btnRejectPressed = false
        /* 수리 접수 버튼을 눌렀을 때, 나머지 검정색(활성화된) 버튼을 하얀색(비활성화)으로 */
        btnBefore.setOnClickListener {
            items = item_init

            btnIngPressed = false
            btnAfterPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnBeforePressed) {
                // 다른 버튼이 눌린 상태에서(필터가 되 있는 상황에서), 이 버튼을 누른다면 (자기 자신을 누른다면은 빼야함)
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
            btnBeforePressed = false
            btnAfterPressed = false
            btnRejectPressed = false

            if(isPressedAnyButton && !btnIngPressed) {
                // 다른 버튼이 눌린 상태에서(필터가 되 있는 상황에서), 이 버튼을 누른다면 (자기 자신을 누른다면은 빼야하는데)
                toggleFilter() // 필터 상태 전환
                applyFilter(myAdapter, "수리 중") // 필터 적용
            }

            toggleFilter() // 필터 상태 전환
            applyFilter(myAdapter, "수리 중") // 필터 적용
            val currentTextColor = btnIng.currentTextColor
            /* 텍스트 컬러가 검은색(활성화)이면 버튼을 눌렀을 때 하얀색(필터 해제 = 전체 보기)으로. */
            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true // 버튼이 눌린 상태
                btnIngPressed = true // 자기 자신이 눌린 상태

                btnIng.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnIng.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }
            /* 텍스트 컬러가 하얀색(비활성화)이면, 버튼을 눌렀을 때 검은색(수리 접수 필터)으로. */
            else {
                isPressedAnyButton = false // 버튼이 안 눌린 상태
                btnIngPressed = false // 자기 자신이 안 눌린 상태
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
                // 다른 버튼이 눌린 상태에서(필터가 되 있는 상황에서), 이 버튼을 누른다면 (자기 자신을 누른다면은 빼야하는데)
                toggleFilter() // 필터 상태 전환
                applyFilter(myAdapter, "수리 완료") // 필터 적용
            }

            toggleFilter() // 필터 상태 전환
            applyFilter(myAdapter, "수리 완료") // 필터 적용
            val currentTextColor = btnAfter.currentTextColor
            /* 텍스트 컬러가 검은색(활성화)이면 버튼을 눌렀을 때 하얀색(필터 해제 = 전체 보기)으로. */
            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true // 버튼이 눌린 상태
                btnAfterPressed = true // 자기 자신이 눌린 상태

                btnAfter.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnAfter.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
                btnReject.setBackgroundResource(R.drawable.rounded_corner_white)
                btnReject.setTextColor(Color.BLACK)
            }
            /* 텍스트 컬러가 하얀색(비활성화)이면, 버튼을 눌렀을 때 검은색(수리 접수 필터)으로. */
            else {
                isPressedAnyButton = false // 버튼이 안 눌린 상태
                btnAfterPressed = false // 자기 자신이 안 눌린 상태
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
                // 다른 버튼이 눌린 상태에서(필터가 되 있는 상황에서), 이 버튼을 누른다면 (자기 자신을 누른다면은 빼야하는데)
                toggleFilter() // 필터 상태 전환
                applyFilter(myAdapter, "반려") // 필터 적용
            }
            toggleFilter() // 필터 상태 전환
            applyFilter(myAdapter, "반려") // 필터 적용
            val currentTextColor = btnReject.currentTextColor
            /* 텍스트 컬러가 검은색(활성화)이면 버튼을 눌렀을 때 하얀색(필터 해제 = 전체 보기)으로. */
            if (currentTextColor == Color.BLACK) {
                isPressedAnyButton = true // 버튼이 눌린 상태
                btnRejectPressed = true // 자기 자신이 눌린 상태

                btnReject.setBackgroundResource(R.drawable.rounded_corner_grey)
                btnReject.setTextColor(Color.WHITE)
                btnBefore.setBackgroundResource(R.drawable.rounded_corner_white)
                btnBefore.setTextColor(Color.BLACK)
                btnAfter.setBackgroundResource(R.drawable.rounded_corner_white)
                btnAfter.setTextColor(Color.BLACK)
                btnIng.setBackgroundResource(R.drawable.rounded_corner_white)
                btnIng.setTextColor(Color.BLACK)
            }
            /* 텍스트 컬러가 하얀색(비활성화)이면, 버튼을 눌렀을 때 검은색(수리 접수 필터)으로. */
            else {
                isPressedAnyButton = false // 버튼이 안 눌린 상태
                btnRejectPressed = false // 자기 자신이 안 눌린 상태
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
    private fun toggleFilter() {
        isFilterApplied = !isFilterApplied // 필터 상태를 토글
    }

    private fun applyFilter(adapter: AdminRoomAdapter, state: String) {
        if (isFilterApplied) {
            // 필터가 적용된 경우, "수리 접수"인 아이템만 보이도록 필터링
            val filteredItems = items.filter { it.status == state }
            adapter.updateList(filteredItems) // 필터된 목록으로 어댑터 업데이트
        } else {
            // 필터가 적용되지 않은 경우, 전체 아이템 표시
            adapter.updateList(items) // 전체 목록으로 어댑터 업데이트
        }
    }

    private fun addDataToList() {
        items.add(AdminRoom("의자 고장", "제1공학관 5층 34호(21534) A-28", R.drawable.baseline_chair_24, "수리 접수", "24-05-06 8:24 p.m.", "제1공학관 5층 34호(21534) A-28 입니다!"))
        items.add(AdminRoom("책상 고장", "제2공학관 1층 24호(22124) A-13", R.drawable.baseline_table_restaurant_24, "반려", "24-05-06 7:15 p.m.", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad."))
        items.add(AdminRoom("콘센트 고장", "제2공학관 1층 24호(22124) A-15", R.drawable.baseline_electrical_services_24, "수리 중", "24-05-06 4:21 p.m.", "수리 필요. 의자."))
        items.add(AdminRoom("의자 고장", "제3공학관 2층 12호(23212) A-5", R.drawable.baseline_chair_24, "수리 완료", "24-05-06 4:05 p.m.", "공백"))
        items.add(AdminRoom("의자 고장", "제1공학관 5층 34호(21534) A-28", R.drawable.baseline_chair_24, "수리 접수", "24-05-06 8:24 p.m.", "제1공학관 5층 34호(21534) A-28 입니다!"))
        items.add(AdminRoom("책상 고장", "제2공학관 1층 24호(22124) A-13", R.drawable.baseline_table_restaurant_24, "반려", "24-05-06 7:15 p.m.", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad."))
        items.add(AdminRoom("콘센트 고장", "제2공학관 1층 24호(22124) A-15", R.drawable.baseline_electrical_services_24, "수리 중", "24-05-06 4:21 p.m.", "수리 필요. 의자."))
        items.add(AdminRoom("의자 고장", "제3공학관 2층 12호(23212) A-5", R.drawable.baseline_chair_24, "수리 완료", "24-05-06 4:05 p.m.", "공백"))
    }

    private fun addDataToList2() {
        item_init.add(AdminRoom("의자 고장", "제1공학관 5층 34호(21534) A-28", R.drawable.baseline_chair_24, "수리 접수", "24-05-06 8:24 p.m.", "제1공학관 5층 34호(21534) A-28 입니다!"))
        item_init.add(AdminRoom("책상 고장", "제2공학관 1층 24호(22124) A-13", R.drawable.baseline_table_restaurant_24, "반려", "24-05-06 7:15 p.m.", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad."))
        item_init.add(AdminRoom("콘센트 고장", "제2공학관 1층 24호(22124) A-15", R.drawable.baseline_electrical_services_24, "수리 중", "24-05-06 4:21 p.m.", "수리 필요. 의자."))
        item_init.add(AdminRoom("의자 고장", "제3공학관 2층 12호(23212) A-5", R.drawable.baseline_chair_24, "수리 완료", "24-05-06 4:05 p.m.", "공백"))
        item_init.add(AdminRoom("의자 고장", "제1공학관 5층 34호(21534) A-28", R.drawable.baseline_chair_24, "수리 접수", "24-05-06 8:24 p.m.", "제1공학관 5층 34호(21534) A-28 입니다!"))
        item_init.add(AdminRoom("책상 고장", "제2공학관 1층 24호(22124) A-13", R.drawable.baseline_table_restaurant_24, "반려", "24-05-06 7:15 p.m.", "Can anybody give me the hint? I will be very happy if you help. If nobody help me, I will be very sad."))
        item_init.add(AdminRoom("콘센트 고장", "제2공학관 1층 24호(22124) A-15", R.drawable.baseline_electrical_services_24, "수리 중", "24-05-06 4:21 p.m.", "수리 필요. 의자."))
        item_init.add(AdminRoom("의자 고장", "제3공학관 2층 12호(23212) A-5", R.drawable.baseline_chair_24, "수리 완료", "24-05-06 4:05 p.m.", "공백"))
    }
}
