package com.skku.fixskkufront

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MyReportFragment : Fragment() {

    private val client = OkHttpClient()

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

        val token = "token1" // Replace with actual token
        fetchReports(token) { reports ->
            recyclerView.adapter = MyReportAdapter(reports)
        }
    }

    private fun fetchReports(token: String, callback: (List<MyReportItem>) -> Unit) {
        val url = "http://13.124.89.169/report/list?token=$token"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MyReportFragment", "Failed to fetch reports", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val reports = parseReports(it)
                        activity?.runOnUiThread {
                            callback(reports)
                        }
                    }
                } else {
                    Log.e("MyReportFragment", "Failed to fetch reports: ${response.message}")
                }
            }
        })
    }

    private fun parseReports(jsonResponse: String): List<MyReportItem> {
        val jsonObject = JSONObject(jsonResponse)
        val data = jsonObject.getJSONObject("data")
        val reportsArray = data.getJSONArray("reports")
        val reports = mutableListOf<MyReportItem>()

        for (i in 0 until reportsArray.length()) {

            val report = reportsArray.getJSONObject(i)
            val reportId = report.getString("reportId")
            val title = report.getString("facilityType")
            val subtitle = "${report.getString("campus")} ${report.getString("building")}"
            val tag1 = report.getString("classroom")
            val tag2 = "A-${report.getInt("facilityId") % 10}"
            val tag3 = report.getString("facilityType")
            val tag5 = report.getString("reportStatus")
            var imageId = R.drawable.icon_home
            when(title){
                "책상" -> imageId = R.drawable.desk
                "의자" -> imageId = R.drawable.chair
                "에어컨" -> imageId = R.drawable.air
                "콘센트" -> imageId = R.drawable.outlet
                else -> imageId = R.drawable.air
            }
            reports.add(MyReportItem(reportId, imageId, title, subtitle, tag1, tag2, tag3, tag5))
        }
        return reports
    }
}

data class MyReportItem(val reportId : String, val imageResId: Int, val title: String, val subtitle: String, val tag1: String, val tag2: String, val tag3: String, val tag5: String)
