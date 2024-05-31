package com.skku.fixskkufront

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class ChatbotActivity : AppCompatActivity() {

    companion object {
        const val EXT_FAQ = "extra_key_FAQ"

        private var instance: ChatbotActivity? = null

        fun getInstance(): ChatbotActivity? {
            return instance
        }
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleFAQButtonClick(faqNumber: Int) {
        // 서버로 FAQ 요청을 전송
        sendFAQRequest(faqNumber)
        // 다음 액티비티로 이동
        val intent = Intent(this, ChatbotActivity2::class.java).apply {
            putExtra(EXT_FAQ, faqNumber.toString())
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        instance = this
    }

    private fun sendFAQRequest(faqNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val urlString = "http://10.0.2.2:8000/test"
            var requestBody = ""
            if (faqNumber == 1) { requestBody = "FAQ 1번 대답입니다." }
            else if (faqNumber == 2) { requestBody = "FAQ 2번 대답입니다." }
            else if (faqNumber == 3) { requestBody = "FAQ 3번 대답입니다." }
            else if (faqNumber == 4) { requestBody = "FAQ 4번 대답입니다." }

            val client = OkHttpClient()
            data class SendFAQ(var text: String ?= null)
            val json = Gson().toJson(SendFAQ(requestBody))
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val req = Request.Builder().url(urlString).post(json.toString().toRequestBody(mediaType)).build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        // 필요한 경우 응답 처리
                    }
                }
            })
        }
    }
}