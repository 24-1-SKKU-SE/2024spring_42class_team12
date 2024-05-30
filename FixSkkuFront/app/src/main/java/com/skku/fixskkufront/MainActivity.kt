package com.skku.fixskkufront

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXT_FAQ = "extra_key_FAQ"

        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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
            // 뒤로 가기 버튼 클릭 처리
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
        val intent = Intent(this, MainActivity2::class.java).apply {
            putExtra(EXT_FAQ, faqNumber.toString())
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        instance = this
    }

    private fun sendFAQRequest(faqNumber: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val urlString = "https://yourserver.com/chatbot?faq=$faqNumber"
            val requestBody = "FAQ $faqNumber 대답"

            var urlConnection: HttpURLConnection? = null

            try {
                val url = URL(urlString)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                urlConnection.setRequestProperty("charset", "utf-8")

                val outputBytes = requestBody.toByteArray(Charsets.UTF_8)
                urlConnection.setRequestProperty("Content-Length", outputBytes.size.toString())

                val outputStream = urlConnection.outputStream
                outputStream.write(outputBytes)
                outputStream.flush()
                outputStream.close()

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 필요한 경우 응답 처리
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        }
    }
}