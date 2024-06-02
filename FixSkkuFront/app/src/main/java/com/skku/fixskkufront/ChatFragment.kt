package com.skku.fixskkufront

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class ChatFragment : Fragment() {

    companion object {
        const val EXT_FAQ = "extra_key_FAQ"
    }

    private lateinit var faqSection: View
    private lateinit var chatSection: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var welcomeTextView: TextView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        faqSection = view.findViewById(R.id.faq_section)
        chatSection = view.findViewById(R.id.chat_section)

        val btnFAQ1 = view.findViewById<Button>(R.id.FAQ_btn_1)
        val btnFAQ2 = view.findViewById<Button>(R.id.FAQ_btn_2)
        val btnFAQ3 = view.findViewById<Button>(R.id.FAQ_btn_3)
        val btnFAQ4 = view.findViewById<Button>(R.id.FAQ_btn_4)

        btnFAQ1.setOnClickListener { handleFAQButtonClick(1) }
        btnFAQ2.setOnClickListener { handleFAQButtonClick(2) }
        btnFAQ3.setOnClickListener { handleFAQButtonClick(3) }
        btnFAQ4.setOnClickListener { handleFAQButtonClick(4) }

        val btnClose = view.findViewById<ImageButton>(R.id.btn_close)
        btnClose.setOnClickListener {
            faqSection.visibility = View.GONE
            chatSection.visibility = View.VISIBLE
            setupChatSection(view)
        }

        val btnBackMain = view.findViewById<ImageButton>(R.id.back_btn_main)
        btnBackMain.setOnClickListener {
            activity?.finish()
        }
    }

    private fun handleFAQButtonClick(faqNumber: Int) {
        faqSection.visibility = View.GONE
        chatSection.visibility = View.VISIBLE
        setupChatSection(requireView())
        handleFAQ(faqNumber.toString())
        hideSystemUI()
        imageView.alpha = 0.2f
    }

    private fun sendFAQRequest(faqNumber: Int) {
        val urlString = "http://13.124.89.169/chatbot?faq=$faqNumber"
        val requestBody = when (faqNumber) {
            1 -> "1. 챗봇에게 신고할 시설물이 있는 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \"인문사회과학캠퍼스 경영관 33404 시설물 신고\"\n" +
                    "2. 챗봇이 해당 내용을 바탕으로 신고 페이지에 강의실 정보를 입력해줄거에요.\n" +
                    "3. 나머지 신고에 필요한 내용을 신고 페이지에서 채워주세요. 사진과 상세 설명은 선택사항이에요.\n" +
                    "4. 신고에 필요한 부분을 입력하셨으면 신고하기 버튼을 눌러주세요.\n" +
                    "5. 신고가 완료되면 자동으로 내 신고페이지로 이동해요. 내 신고에서 신고내역을 확인할 수 있어요."
            2 -> "1. 챗봇에게 \"내 신고 조회\" 라고 입력해주세요.\n" +
                    "2. 챗봇이 내 신고 페이지로 이동시켜줄거에요.\n" +
                    "3. 내 신고 페이지에서 신고 내역을 조회할 수 있어요."
            3 -> "1. 챗봇에게 조회할 강의실의 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \"인문사회과학캠퍼스 경영관 33404  강의실 정보 조회\"\n" +
                    "2. 챗봇이 해당 내용을 바탕으로 시설물 상태 페이지의 해당 강의실 정보 페이지로 이동시켜줄거에요.\n" +
                    "3. 강의실에서 초록색으로 표시된 부분이 사용가능한 상태를, 빨간 색으로 표시된 부분이 고장난 상태를 나타내요."
            4 -> "1. 챗봇에게 \"시설물 담당자 연락처\" 라고 입력해주세요.\n" +
                    "2. 챗봇이 시설물 담당자의 연락처를 알려줄거에요."
            else -> ""
        }
        val client = OkHttpClient()

        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("text", requestBody)
        val reqBody = builder.build()

        val req = Request.Builder().url(urlString)
            .addHeader("token", "token1")
            .addHeader("Accept", "application/json")
            .post(reqBody).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    // 필요한 경우 응답 처리
                }
            }
        })
    }

    private fun setupChatSection(view: View) {
        messageList = mutableListOf()

        recyclerView = view.findViewById(R.id.recycler_view)
        welcomeTextView = view.findViewById(R.id.welcome_text)
        messageEditText = view.findViewById(R.id.message_edit_text)
        sendButton = view.findViewById(R.id.send_btn)
        imageView = view.findViewById(R.id.SKKU_imageView)
        backButton = view.findViewById(R.id.back_btn)

        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        sendButton.setOnClickListener {
            val question = messageEditText.text.toString().trim()
            addToChat(question, Message.SENT_BY_ME)
            messageEditText.setText("")
            hideSystemUI()
            sendMessageToServer(question)

            imageView.alpha = 0.2f
        }

        backButton.setOnClickListener {
            faqSection.visibility = View.VISIBLE
            chatSection.visibility = View.GONE
        }

        val faqNumber = arguments?.getString(EXT_FAQ)
        if (faqNumber != null) {
            handleFAQ(faqNumber)
        }
    }

    private fun addToChat(message: String, sentBy: String) {
        activity?.runOnUiThread {
            messageList.add(Message(message, sentBy))
            messageAdapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
        }
    }

    private fun handleFAQ(faqNumber: String) {
        val message = when (faqNumber) {
            "1" -> "1. 챗봇에게 신고할 시설물이 있는 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \"인문사회과학캠퍼스 경영관 33404 시설물 신고\"\n" +
                    "2. 챗봇이 해당 내용을 바탕으로 신고 페이지에 강의실 정보를 입력해줄거에요.\n" +
                    "3. 나머지 신고에 필요한 내용을 신고 페이지에서 채워주세요. 사진과 상세 설명은 선택사항이에요.\n" +
                    "4. 신고에 필요한 부분을 입력하셨으면 신고하기 버튼을 눌러주세요.\n" +
                    "5. 신고가 완료되면 자동으로 내 신고페이지로 이동해요. 내 신고에서 신고내역을 확인할 수 있어요."
            "2" -> "1. 챗봇에게 \"내 신고 조회\" 라고 입력해주세요.\n" +
                    "2. 챗봇이 내 신고 페이지로 이동시켜줄거에요.\n" +
                    "3. 내 신고 페이지에서 신고 내역을 조회할 수 있어요."
            "3" -> "1. 챗봇에게 조회할 강의실의 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \"인문사회과학캠퍼스 경영관 33404  강의실 정보 조회\"\n" +
                    "2. 챗봇이 해당 내용을 바탕으로 시설물 상태 페이지의 해당 강의실 정보 페이지로 이동시켜줄거에요.\n" +
                    "3. 강의실에서 초록색으로 표시된 부분이 사용가능한 상태를, 빨간 색으로 표시된 부분이 고장난 상태를 나타내요."
            "4" -> "1. 챗봇에게 \"시설물 담당자 연락처\" 라고 입력해주세요.\n" +
                    "2. 챗봇이 시설물 담당자의 연락처를 알려줄거에요."
            else -> ""
        }
        addToChat(message, Message.SENT_BY_BOT)
    }

    private fun sendMessageToServer(message: String) {
        Log.d("Log", "sendMessageToServer")
        val urlString = "http://13.124.89.169/chatbot"
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("text", message)
            .build()

        val request = Request.Builder()
            .url(urlString)
            .addHeader("token", "token1")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("ChatRB", responseBody.toString())
                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                    val data = jsonObject.getAsJsonObject("data")
                    Log.d("JSON-response", jsonObject.toString())
                    activity?.runOnUiThread {
                        when (jsonObject.get("message").asString) {
                            "챗봇 자주 묻는 질문 응답 성공" -> {
                                // Handle FAQ response
                                addToChat(message, Message.SENT_BY_BOT)
                                handleGeneralQuestionResponse(data)
                                handleFAQResponse(data)
                            }
                            "챗봇 일반 질문 응답 성공" -> {
                                // Handle general question response
                                val message = data.get("response").asString
                                addToChat(message, Message.SENT_BY_BOT)
                                handleGeneralQuestionResponse(data)
                            }
                            "챗봇 망가진 시설물 정보 조회 성공" -> {
                                // Handle faulty facilities response
                                handleFaultyFacilitiesResponse(data)
                            }
                            "챗봇 신고 요청 성공" -> {
                                // Handle report request response
                                handleReportRequestResponse(data)
                            }
                            "챗봇 자신의 신고 조회 성공" -> {
                                // Handle own reports response
                                handleOwnReportsResponse(data)
                            }
                            else -> {
                                Log.d("ChatResponse", "Unknown response type")
                            }
                        }
                    }
                }
            }
        })
    }

    private fun handleFAQResponse(data: JsonObject) {
        val message = data.get("response").asString
        addToChat(message, Message.SENT_BY_BOT)
    }

    private fun handleGeneralQuestionResponse(data: JsonObject) {
        val message = data.get("response").asString
        addToChat(message, Message.SENT_BY_BOT)
    }

    private fun handleFaultyFacilitiesResponse(data: JsonObject) {
        navigateToUserActivity("시설물 조회")
    }

    private fun handleReportRequestResponse(data: JsonObject) {
        navigateToUserActivity("신고 요청")
    }

    private fun handleOwnReportsResponse(data: JsonObject) {
        navigateToUserActivity("신고 조회")
    }

    private fun navigateToUserActivity(action: String) {
        val intent = Intent(requireContext(), UserActivity::class.java)
        intent.action = action
        startActivity(intent)
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.navigationBars())
            requireActivity().window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
