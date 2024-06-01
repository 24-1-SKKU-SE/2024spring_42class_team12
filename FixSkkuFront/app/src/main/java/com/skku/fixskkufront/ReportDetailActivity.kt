package com.skku.fixskkufront

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ReportDetailActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val reportId = intent.getStringExtra("report_id")
        val token = "token1"  // 실제 토큰으로 대체

        if (reportId != null) {
            sendGetRequest(reportId, token)
        } else {
            Toast.makeText(this, "Invalid report ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendGetRequest(reportId: String, token: String) {
        val url = "http://13.124.89.169/report/$reportId?token=$token"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ReportDetailActivity", "Failed to send GET request", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("rB",responseBody.toString())
                    responseBody?.let {
                        val reportData = parseReportData(it)
                        runOnUiThread {
                            updateUI(reportData)
                        }
                    }
                } else {
                    Log.e("ReportDetailActivity", "Failed to get response: ${response.message}")
                }
            }
        })
    }

    private fun parseReportData(jsonResponse: String): Data {
        val jsonObject = JSONObject(jsonResponse)
        val data = jsonObject.getJSONObject("data")
        val report = data.getJSONObject("report")

        return Data(
            reportID =  report.getString("reportId"),
            campus = report.getString("campus"),
            building = report.getString("building"),
            classroom = report.getString("classroom"),
            facilityId = report.getString("facilityId"),
            faultType = report.getString("facilityType"),
            description = report.optString("description")
        )
    }

    private fun updateUI(reportData: Data) {
        findViewById<EditText>(R.id.campusName).setText(reportData.campus)
        findViewById<EditText>(R.id.classroomName).setText(reportData.classroom)
        findViewById<EditText>(R.id.facilityId).setText(reportData.facilityId)
        findViewById<EditText>(R.id.faultType).setText(reportData.faultType)
        findViewById<EditText>(R.id.additionalInfo).setText(reportData.description)
    }
}

data class Data(
    val reportID : String,
    val campus: String,
    val building: String,
    val classroom: String,
    val facilityId: String,
    val faultType: String,
    val description: String
)
