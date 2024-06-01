package com.skku.fixskkufront

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyReportAdatper(getDummyData())
    }

    private fun getDummyData(): List<MyReportItem> {
        // Example data
        return listOf(
            MyReportItem(R.drawable.icon_home, "의자 등받이 부러짐", "자연과학캠퍼스 제2공학관", "27232", "A-32", "의자 고장"),
            MyReportItem(R.drawable.icon_home, "콘센트 충전 불가", "자연과학캠퍼스 제2공학관", "26123", "A-12", "콘센트 고장"),
            MyReportItem(R.drawable.icon_home, "의자 등받이 부러짐", "자연과학캠퍼스 제2공학관", "31312", "A-31", "의자 고장")
        )
    }
}

data class MyReportItem(val imageResId: Int, val title: String, val subtitle: String, val tag1: String, val tag2: String, val tag3: String)
