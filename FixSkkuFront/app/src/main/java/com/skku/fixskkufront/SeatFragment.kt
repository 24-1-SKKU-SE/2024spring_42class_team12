package com.skku.fixskkufront

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
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

        // Setup ExpandableListView
        val expandableListView: ExpandableListView = view.findViewById(R.id.expandableListView)
        val listData = getListData()
        val expandableListAdapter = CustomExpandableListAdapter(requireContext(), listData)
        expandableListView.setAdapter(expandableListAdapter)

        // Setup RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.seatRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 11)
        recyclerView.adapter = SeatAdapter(getSeatData())
    }

    private fun getListData(): List<Pair<String, List<String>>> {
        // Example data for ExpandableListView
        return listOf(
            Pair("자연과학캠퍼스", listOf("강의실 1", "강의실 2", "강의실 3")),
            Pair("제2공학관", listOf("강의실 1", "강의실 2", "강의실 3")),
            Pair("강의실 번호를 선택하십시오", listOf("강의실 1", "강의실 2", "강의실 3"))
        )
    }

    private fun getSeatData(): List<Seat> {
        // Example data for seats
        return List(99) { index ->
            when (index % 11) {
                1,2 -> Seat(R.drawable.seat_green) // green seat
                4,5,6-> Seat(R.drawable.seat_red)  // red seat
                0, 3, 7, 10 -> Seat(isEmpty = true) // empty seat
                else -> Seat(R.drawable.seat_black) // black seat
            }
        }
    }
}

data class Seat(val imageResId: Int? = null, val isEmpty: Boolean = false)
