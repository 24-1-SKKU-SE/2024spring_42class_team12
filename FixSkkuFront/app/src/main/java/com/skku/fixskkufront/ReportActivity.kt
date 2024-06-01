//
//package com.skku.fixskkufront
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.toRequestBody
//import org.json.JSONObject
//import java.io.IOException
//
//class ReportActivity : AppCompatActivity() {
//    private val client = OkHttpClient()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_report)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        val backBtn = findViewById<ImageView>(R.id.backBtn)
//        backBtn.setOnClickListener {
//            finish() // 현재 Activity를 종료하여 이전 화면으로 돌아갑니다.
//        }
//
//        val campusType = intent.getIntExtra("campusType", 0)
//        val buildingName = intent.getStringExtra("building_name")
//        val classRoomName = intent.getStringExtra("classRoomName")
//        val reportButton = findViewById<Button>(R.id.reportButton)
//
//        reportButton.setOnClickListener {
//            val json = JSONObject()
//            json.put("token", "token1")
//            json.put("campus", "SEOUL_CAMPUS")
//            json.put("building", "HOAM")
//            json.put("floor", "5") // 필요한 값을 넣어줍니다.
//            json.put("classroom", "66011")
//            json.put("facilityType", "DESK") // 필요한 값을 넣어줍니다.
//            json.put("facilityId", "8") // 필요한 값을 넣어줍니다.
//            json.put("description", "aaa")
//            json.put("photoUrl", "")
//
//            postReport(json.toString())
//        }
//    }
//
//    private fun postReport(json: String) {
//        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .url("http://13.124.89.169:8081/report")
//            .post(requestBody)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Log.d("Fail", e.toString())
//                    Toast.makeText(this@ReportActivity, "Failed to send report", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                Log.d("Res ", response.toString())
//                response.use {
//                    if (response.isSuccessful) {
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@ReportActivity,
//                                "Report sent successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    } else {
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@ReportActivity,
//                                "Failed to send report: ${response.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//        })
//    }
//}


/*
package com.skku.fixskkufront

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import java.io.IOException

class ReportActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish() // 현재 Activity를 종료하여 이전 화면으로 돌아갑니다.
        }

        val reportButton = findViewById<Button>(R.id.reportButton)

        reportButton.setOnClickListener {
            val token = "token1"
            getReports(token)
        }
    }

    private fun getReports(token: String) {
        val url = "http://13.124.89.169:8081/report/list?token=$token"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ReportActivity, "Failed to fetch reports", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { // use 블록을 사용하여 응답 본문을 자동으로 닫습니다.
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        runOnUiThread {
                            Toast.makeText(this@ReportActivity, "Reports fetched successfully", Toast.LENGTH_SHORT).show()
                            // 응답 데이터를 사용하여 필요한 작업을 수행합니다.
                            // 예를 들어, 로그로 출력합니다.
                            println("Response Data: $responseData")
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@ReportActivity, "Failed to fetch reports: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
*/


package com.skku.fixskkufront

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import java.io.IOException

class ReportActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish() // 현재 Activity를 종료하여 이전 화면으로 돌아갑니다.
        }

        val reportButton = findViewById<Button>(R.id.reportButton)

        reportButton.setOnClickListener {
            val classRoomName = "77077" // 예시 강의실 번호
            val token = "token1" // 실제 토큰 값
            getFacilityInfo(classRoomName, token)
        }
    }

    private fun getFacilityInfo(classRoomName: String, token: String) {
        val url = "http://13.124.89.169:8081/fac/$classRoomName"
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("token", token) // 헤더에 토큰 추가
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ReportActivity, "Failed to fetch facility info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { // use 블록을 사용하여 응답 본문을 자동으로 닫습니다.
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        runOnUiThread {
                            Toast.makeText(this@ReportActivity, "Facility info fetched successfully", Toast.LENGTH_SHORT).show()
                            // 응답 데이터를 사용하여 필요한 작업을 수행합니다.
                            // 예를 들어, 로그로 출력합니다.
                            println("Response Data: $responseData")
                        }
                    } else {
                        runOnUiThread {
                            Log.d("ttt",response.toString())
                            Toast.makeText(this@ReportActivity, "Failed to fetch facility info: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}

