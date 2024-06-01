package com.skku.fixskkufront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ChatbotActivity : AppCompatActivity() {

    companion object {
        const val EXT_FAQ = "extra_key_FAQ"
        private var instance: ChatbotActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatmain)

        val btnFAQ1 = findViewById<Button>(R.id.FAQ_btn_1)
        val btnFAQ2 = findViewById<Button>(R.id.FAQ_btn_2)
        val btnFAQ3 = findViewById<Button>(R.id.FAQ_btn_3)
        val btnFAQ4 = findViewById<Button>(R.id.FAQ_btn_4)

        btnFAQ1.setOnClickListener { handleFAQButtonClick(1) }
        btnFAQ2.setOnClickListener { handleFAQButtonClick(2) }
        btnFAQ3.setOnClickListener { handleFAQButtonClick(3) }
        btnFAQ4.setOnClickListener { handleFAQButtonClick(4) }

        val btnBack = findViewById<ImageButton>(R.id.back_btn_main)
        btnBack.setOnClickListener {
            finish()
        }

        val btnClose = findViewById<ImageButton>(R.id.btn_close)
        btnClose.setOnClickListener {
            val intent = Intent(this, ChatbotActivity2::class.java).apply {
                putExtra(EXT_FAQ, "0")
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            instance = this
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleFAQButtonClick(faqNumber: Int) {
        sendFAQRequest(faqNumber)
        val intent = Intent(this, ChatbotActivity2::class.java).apply {
            putExtra(EXT_FAQ, faqNumber.toString())
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        instance = this
    }

    private fun sendFAQRequest(faqNumber: Int) {
        val urlString = "http://13.124.89.169/chatbot?faq=$faqNumber"
        var requestBody = ""
        if (faqNumber == 1) { requestBody = "1. 챗봇에게 신고할 시설물이 있는 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \\\"인문사회과학캠퍼스 경영관 33404 시설물 신고\\\"\\n\" +\n" +
                "                    \"2. 챗봇이 해당 내용을 바탕으로 신고 페이지에 강의실 정보를 입력해줄거에요.\" +\n" +
                "                    \"3. 나머지 신고에 필요한 내용을 신고 페이지에서 채워주세요. 사진과 상세 설명은 선택사항이에요.\\n\" +\n" +
                "                    \"4. 신고에 필요한 부분을 입력하셨으면 신고하기 버튼을 눌러주세요.\\n\" +\n" +
                "                    \"5. 신고가 완료되면 자동으로 내 신고페이지로 이동해요. 내 신고에서 신고내역을 확인할 수 있어요.\\n" }
        else if (faqNumber == 2) { requestBody = "1. 챗봇에게 \\\"내 신고 조회\\\" 라고 입력해주세요.\\n\" +\n" +
                "                    \"2. 챗봇이 내 신고 페이지로 이동시켜줄거에요.\\n\" +\n" +
                "                    \"3. 내 신고 페이지에서 신고 내역을 조회할 수 있어요.\\n" }
        else if (faqNumber == 3) { requestBody = "1. 챗봇에게 조회할 강의실의 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \\\"인문사회과학캠퍼스 경영관 33404  강의실 정보 조회\\\"\\n\" +\n" +
                "                    \"2. 챗봇이 해당 내용을 바탕으로 시설물 상태 페이지의 해당 강의실 정보 페이지로 이동시켜줄거에요.\\n\" +\n" +
                "                    \"3. 강의실에서 초록색으로 표시된 부분이 사용가능한 상태를, 빨간 색으로 표시된 부분이 고장난 상태를 나타내요.\\n" }
        else if (faqNumber == 4) { requestBody = "1. 챗봇에게 \\\"시설물 담당자 연락처\\\" 라고 입력해주세요.\\n\" +\n" +
                "                    \"2. 챗봇이 시설물 담당자의 연락처를 알려줄거에요.\\n" }
        val client = OkHttpClient()

        data class SendFAQ(var text: String = "")
        val json = Gson().toJson(SendFAQ(requestBody))
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val req = Request.Builder().url(urlString)
            .addHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIiwianRpIjoiMTRkOTRmYjgtNzNmMi00Mzc0LWI0MGYtZWJhNWNkNmI3M2U2IiwiaWF0IjoxNzE2NjM5ODY3fQ.10427Pg37n_IEeo41t5OJVsb5VgM8CMMJBa14v7ZC")
            .addHeader("Accept", "application/json")
            .post(json.toString().toRequestBody(mediaType)).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    //if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    // 필요한 경우 응답 처리
                }
            }
        })
    }
}