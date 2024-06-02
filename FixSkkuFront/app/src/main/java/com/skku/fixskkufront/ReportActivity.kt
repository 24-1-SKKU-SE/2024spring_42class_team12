package com.skku.fixskkufront

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

//        val reportDate = findViewById<EditText>(R.id.reportDate)
        val campusName = findViewById<EditText>(R.id.campusName)
        val classroomName = findViewById<EditText>(R.id.classroomName)
        val facilityId = findViewById<EditText>(R.id.facilityId)
        val faultType = findViewById<EditText>(R.id.faultType)
        val isUserFacility = findViewById<CheckBox>(R.id.isUserFacility)
        val additionalInfo = findViewById<EditText>(R.id.additionalInfo)
        val reportButton = findViewById<Button>(R.id.reportButton)

        val campusType = intent.getIntExtra("campusType", 0)
        val buildingName = intent.getStringExtra("building_name")
        val classRoomName = intent.getStringExtra("classRoomName")
        val selectedSeatIndex = intent.getIntExtra("selected_seat_index", -1) + 1

        var campus = "SUWON_CAMPUS"
        var building = "PHARM"
        var floor = "3"
        var classroom = classRoomName
        var facilityType = "DESK"
        var facility = selectedSeatIndex.toString()
        var desc = "PH"
        var photoUrl = ""

        if(campusType == 0){
            campusName.setText("인문사회캠퍼스")
        }
        else{
            campusName.setText("자연과학캠퍼스")
        }

        classroomName.setText(classRoomName)
        facilityId.setText(classRoomName + selectedSeatIndex)
        // 로그 출력
        Log.d("ReportActivity", "campusType: $campusType")
        Log.d("ReportActivity", "buildingName: $buildingName")
        Log.d("ReportActivity", "classRoomName: $classRoomName")
        Log.d("ReportActivity", "selectedSeatIndex: $selectedSeatIndex")

        reportButton.setOnClickListener {
            if (classroom != null) {
                postReport(
                    token = "token1",
                    campus = campus,
                    building = building,
                    floor = floor, // 필요한 값을 넣어줍니다.
                    classroom = classroom,
                    facilityType = facilityType, // 필요한 값을 넣어줍니다.
                    facilityId = classroom+facility, // 필요한 값을 넣어줍니다.
                    description =desc,
                    photoUrl = ""
                )
            }

        }
    }

    private fun postReport(
        token: String,
        campus: String,
        building: String,
        floor: String,
        classroom: String,
        facilityType: String,
        facilityId: String,
        description: String,
        photoUrl: String
    ) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", token)
            .addFormDataPart("campus", campus)
            .addFormDataPart("building", building)
            .addFormDataPart("floor", floor)
            .addFormDataPart("classroom", classroom)
            .addFormDataPart("facilityType", facilityType)
            .addFormDataPart("facilityId", facilityId)
            .addFormDataPart("description", description)
            .addFormDataPart("photoUrl", photoUrl)
            .build()

        val request = Request.Builder()
            .url("http://13.124.89.169/report")
            .post(requestBody)
            .build()
        Log.d("request",request.toString())
        Log.d("body",requestBody.toString())
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Log.d("Fail", e.toString())
                    Toast.makeText(this@ReportActivity, "Failed to send report", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("Res", response.toString())
                response.use {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                this@ReportActivity,
                                "Report sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@ReportActivity,
                                "Failed to send report: ${response.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }
}
