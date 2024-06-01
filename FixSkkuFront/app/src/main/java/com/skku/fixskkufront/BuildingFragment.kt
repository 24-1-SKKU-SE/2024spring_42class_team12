package com.skku.fixskkufront

import android.os.Bundle
import android.util.Log
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
        val campusType = arguments?.getInt("campusType")
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        Log.d("campusType", campusType.toString())
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = BuildingAdapter(getBuildings(campusType), requireActivity(), campusType)
    }

    private fun getBuildings(campusType : Int?): List<Building> {
        // 예제 데이터를 생성합니다.
        return if(campusType == 0){
            listOf(
                Building("경영관", R.drawable.fundamentalstudy),
                Building("다산경제관", R.drawable.fundamentalstudy),
                Building("법학관", R.drawable.fundamentalstudy),
                Building("수선관", R.drawable.fundamentalstudy),
                Building("*퇴계인문관", R.drawable.fundamentalstudy),
                Building("*호암관", R.drawable.fundamentalstudy)
            )
        }
        else{
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

}
data class Building(val name: String, val imageResId: Int)