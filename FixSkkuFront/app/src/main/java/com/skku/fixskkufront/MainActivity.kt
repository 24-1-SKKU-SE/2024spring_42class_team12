package com.skku.fixskkufront

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    companion object{
        const val EXT_FAQ = "extra_key_FAQ"

        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }

    }
    /* 수정해야할 것 : 아이콘 , 버튼 디자인 , 채팅 재진입 시 초기화 문제 , 인삿말을 말풍선 모양으로 변경, 맨 밑에 하단바 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        /* FAQ 1번을 클릭하면 채팅룸으로 넘어가면서 봇이 이미 저장된 답변을 해준다.
        *  나머지 버튼도 같은 방식으로 제작 예정 */
        val btnFAQ1 = findViewById<Button>(R.id.FAQ_btn_1)
        btnFAQ1.setOnClickListener {

            val intent = Intent(this, MainActivity2::class.java).apply{
                putExtra(EXT_FAQ, "1")
            }
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //intent.setAction("com.example.ACTION_VIEW")
            /* 챗봇 화면에서 뒤로가기 누른 다음에, 다시 FAQ를 눌러서 진입했을 때
            * 이전 대화내용을 뜨게 만들고 싶은데, 아직 구현 못함. */
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent)
            instance = this
        }

        val btnBack = findViewById<ImageButton>(R.id.back_btn_main)
        btnBack.setOnClickListener {

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}