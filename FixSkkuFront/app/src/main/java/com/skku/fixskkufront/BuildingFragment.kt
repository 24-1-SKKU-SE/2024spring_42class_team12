package com.skku.fixskkufront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BuildingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_building, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = BuildingAdapter(getBuildings(), requireActivity())
    }

    private fun getBuildings(): List<Building> {
        // 예제 데이터를 생성합니다.
        return listOf(
            Building("삼성학술정보관", R.drawable.samsunglib),
            Building("N센터", R.drawable.ncenter),
            Building("화학관", R.drawable.chemistry),
            Building("약학관", R.drawable.medicine),
            Building("제1공학관", R.drawable.firstengine),
            Building("제2공학관", R.drawable.secondengine),
            Building("제1과학관", R.drawable.firstscience),
            Building("기초학문관", R.drawable.fundamentalstudy)

        )
    }

}
data class Building(val name: String, val imageResId: Int)