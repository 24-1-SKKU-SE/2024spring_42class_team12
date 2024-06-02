package com.skku.fixskkufront

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class CurrentFragment : Fragment() {
    private val client = OkHttpClient()
    private var selectedSeatIndex: Int? = null
    private val actualSeatIndices = mutableListOf<Int>() // 실제 좌석 인덱스 리스트
    private lateinit var seats: List<Seat>
    private lateinit var adapter: SeatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val campusSpinner: Spinner = view.findViewById(R.id.campusSpinner)
        val buildingSpinner: Spinner = view.findViewById(R.id.buildingSpinner)
        val classRoomEditText: EditText = view.findViewById(R.id.classroomName)

        setupCampusSpinner(campusSpinner)
        setupBuildingSpinner(buildingSpinner, 1) // 기본값 0 (캠퍼스 선택 기본값)
        hideSystemUI()
        // Spinner 선택 이벤트 리스너 설정
        campusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                setupBuildingSpinner(buildingSpinner, position)
                hideSystemUI() // Spinner 선택 후 하단 바 숨기기
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                hideSystemUI() // 아무것도 선택되지 않은 경우에도 하단 바 숨기기
            }
        }

        buildingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                hideSystemUI() // Spinner 선택 후 하단 바 숨기기
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                hideSystemUI() // 아무것도 선택되지 않은 경우에도 하단 바 숨기기
            }
        }
        // Setup RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.seatRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 11)
        seats = getSeatData().toMutableList()
        seats.forEachIndexed { index, seat ->
            if (!seat.isEmpty) {
                actualSeatIndices.add(index)
            }
        }
        adapter = SeatAdapter(seats, requireActivity()) { index ->
            selectedSeatIndex = actualSeatIndices.indexOf(index)
        }
        recyclerView.adapter = adapter

        // Setup Query Button
        val queryButton: Button = view.findViewById(R.id.queryButton)
        queryButton.setOnClickListener {
            val classRoomName = classRoomEditText.text.toString()
            hideSystemUI()
            sendGetRequest(classRoomName)
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.navigationBars())
            requireActivity().window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun setupCampusSpinner(spinner: Spinner) {
        val campuses = listOf("자연과학캠퍼스", "인문사회캠퍼스")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, campuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupBuildingSpinner(spinner: Spinner, campusType: Int) {
        val buildings = getBuildings(campusType).map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, buildings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun sendGetRequest(classRoomName: String) {
        val url = "http://13.124.89.169/fac/$classRoomName"
        val request = Request.Builder().url(url).addHeader("token", "token1")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CurrentFragment", "Failed to send GET request", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("CurrentFragment", "Response: $responseBody")

                    responseBody?.let {
                        val faultySeats = parseFaultySeats(it)
                        updateSeatImages(faultySeats)
                    }
                } else {
                    Log.e("CurrentFragment", "Failed to get response: ${response.message}")
                }
            }
        })
    }

    private fun parseFaultySeats(jsonResponse: String): List<Int> {
        val jsonObject = JSONObject(jsonResponse)
        val dataArray = jsonObject.getJSONArray("data")
        val faultySeatIndices = mutableListOf<Int>()

        for (i in 0 until dataArray.length()) {
            val facility = dataArray.getJSONObject(i)
            if (facility.getString("facilityStatus") != "정상") {
                val facilityId = facility.getInt("facilityId")
                val seatIndex = facilityId % 10 // facilityId의 마지막 4자리
                faultySeatIndices.add(seatIndex)
            }
        }
        Log.d("faulty",faultySeatIndices.toString())
        return faultySeatIndices
    }

    private fun updateSeatImages(faultySeatIndices: List<Int>) {
        activity?.runOnUiThread {
            seats.forEachIndexed { index, seat ->
                if (index in faultySeatIndices) {
                    seat.isEmpty = false
                    seat.imageResId = R.drawable.seat_red
                } else {
                    seat.isEmpty = false
                    when (index % 11) {
                        0, 3, 7, 10 -> seat.isEmpty = true // empty seat
                    }
                    seat.imageResId = R.drawable.seat_green
                }
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun getBuildings(campusType: Int): List<Building> {
        return if (campusType == 1) {
            listOf(
                Building("경영관", R.drawable.fundamentalstudy),
                Building("다산경제관", R.drawable.fundamentalstudy),
                Building("법학관", R.drawable.fundamentalstudy),
                Building("수선관", R.drawable.fundamentalstudy),
                Building("퇴계인문관", R.drawable.fundamentalstudy),
                Building("호암관", R.drawable.fundamentalstudy)
            )
        } else {
            listOf(
                Building("삼성학술정보관", R.drawable.samsunglib),
                Building("N센터", R.drawable.ncenter),
                Building("화학관", R.drawable.chemistry),
                Building("약학관", R.drawable.medicine),
                Building("제1공학관", R.drawable.firstengine),
                Building("제2공학관", R.drawable.secondengine),
                Building("제1과학관", R.drawable.firstscience),
                Building("기초학문관", R.drawable.fundamentalstudy),
                Building("생명공학관", R.drawable.samsunglib),
                Building("수성관", R.drawable.samsunglib),
                Building("반도체관", R.drawable.chemistry)
            )
        }
    }

    private fun getSeatData(): List<Seat> {
        // Example data for seats
        return List(99) { index ->
            when (index % 11) {
                0, 3, 7, 10 -> Seat(isEmpty = true) // empty seat
                else -> Seat(isEmpty = true) // initially all seats are empty
            }
        }
    }


}
