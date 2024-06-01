package com.skku.fixskkufront

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SeatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val campusType = arguments?.getInt("campusType") ?: 0
        var buildingName = arguments?.getString("building_name") ?: ""

        // Setup Spinners and EditText
        val campusSpinner: Spinner = view.findViewById(R.id.campusSpinner)
        val buildingSpinner: Spinner = view.findViewById(R.id.buildingSpinner)
        val classRoomEditText: EditText = view.findViewById(R.id.classroomName)

        setupCampusSpinner(campusSpinner)
        setupBuildingSpinner(buildingSpinner, campusType)

        // Setup RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.seatRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 11)
        recyclerView.adapter = SeatAdapter(getSeatData(), requireActivity())

        // Setup Next Button
        val nextButton: Button = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            val selectedCampus = campusSpinner.selectedItem.toString()
            val selectedBuilding = buildingSpinner.selectedItem.toString()
            val classRoomName = classRoomEditText.text.toString()

            openNextActivity(campusType, selectedBuilding, classRoomName)
        }
    }

    private fun setupCampusSpinner(spinner: Spinner) {
        val campuses = listOf("인문사회캠퍼스", "자연과학캠퍼스")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, campuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupBuildingSpinner(spinner: Spinner, campusType: Int?) {
        val buildings = getBuildings(campusType).map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, buildings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun openNextActivity(campusType: Int, buildingName: String, classRoomName: String) {
        val intent = Intent(requireActivity(), ReportActivity::class.java)
        intent.putExtra("campusType", campusType)
        intent.putExtra("building_name", buildingName)
        intent.putExtra("classRoomName", classRoomName)
        startActivity(intent)
    }

    private fun getBuildings(campusType: Int?): List<Building> {
        return if (campusType == 0) {
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
                1, 2 -> Seat(R.drawable.seat_green) // green seat
                4, 5, 6 -> Seat(R.drawable.seat_red)  // red seat
                0, 3, 7, 10 -> Seat(isEmpty = true) // empty seat
                else -> Seat(R.drawable.seat_black) // black seat
            }
        }
    }
}

data class Seat(val imageResId: Int? = null, val isEmpty: Boolean = false)
