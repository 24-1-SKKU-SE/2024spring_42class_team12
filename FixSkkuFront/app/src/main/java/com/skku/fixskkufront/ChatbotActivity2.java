package com.skku.fixskkufront;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.*;

public class ChatbotActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    ImageButton backButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    ImageView imageView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        imageView = findViewById(R.id.SKKU_imageView);
        backButton = findViewById(R.id.back_btn);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        intent = getIntent();
        String FAQ = intent.getStringExtra(ChatbotActivity.EXT_FAQ);
        if (FAQ != null && FAQ.equals("1")) {
            imageView.setAlpha(0.2f);
            addToChat("1. 챗봇에게 신고할 시설물이 있는 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \"인문사회과학캠퍼스 경영관 33404 시설물 신고\"\n" +
                    "2. 챗봇이 해당 내용을 바탕으로 신고 페이지에 강의실 정보를 입력해줄거에요.\n" +
                    "3. 나머지 신고에 필요한 내용을 신고 페이지에서 채워주세요. 사진과 상세 설명은 선택사항이에요.\n" +
                    "4. 신고에 필요한 부분을 입력하셨으면 신고하기 버튼을 눌러주세요.\n" +
                    "5. 신고가 완료되면 자동으로 내 신고페이지로 이동해요. 내 신고에서 신고내역을 확인할 수 있어요.",Message.SENT_BY_BOT);
        }
        if (FAQ != null && FAQ.equals("2")) {
            imageView.setAlpha(0.2f);
            addToChat("1. 챗봇에게 \"내 신고 조회\" 라고 입력해주세요.\n" +
                    "2. 챗봇이 내 신고 페이지로 이동시켜줄거에요.\n" +
                    "3. 내 신고 페이지에서 신고 내역을 조회할 수 있어요.",Message.SENT_BY_BOT);
        }
        if (FAQ != null && FAQ.equals("3")) {
            imageView.setAlpha(0.2f);
            addToChat("1. 챗봇에게 조회할 강의실의 캠퍼스, 건물, 강의실 번호를 입력해주세요. ex) \"인문사회과학캠퍼스 경영관 33404  강의실 정보 조회\"\n" +
                    "2. 챗봇이 해당 내용을 바탕으로 시설물 상태 페이지의 해당 강의실 정보 페이지로 이동시켜줄거에요.\n" +
                    "3. 강의실에서 초록색으로 표시된 부분이 사용가능한 상태를, 빨간 색으로 표시된 부분이 고장난 상태를 나타내요.",Message.SENT_BY_BOT);
        }
        if (FAQ != null && FAQ.equals("4")) {
            imageView.setAlpha(0.2f);
            addToChat("1. 챗봇에게 \"시설물 담당자 연락처\" 라고 입력해주세요.\n" +
                    "2. 챗봇이 시설물 담당자의 연락처를 알려줄거에요.",Message.SENT_BY_BOT);
        }

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME);
            messageEditText.setText("");
            sendMessageToServer(question);
            imageView.setAlpha(0.2f);

        });

        backButton.setOnClickListener((v)->{
            finish();
        });
    }

    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
    static class SendNormal {
        String text;

        public SendNormal(String text) {
            this.text = text;
        }
    }
    void sendMessageToServer(String message) {
        String urlString = "http://13.124.89.169:8080/chatbot";
        SendNormal sendNormal = new SendNormal(message);
        Gson gson = new Gson();
        String json = gson.toJson(sendNormal);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request req = new Request.Builder()
                .url(urlString)
                .addHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIiwianRpIjoiMTRkOTRmYjgtNzNmMi00Mzc0LWI0MGYtZWJhNWNkNmI3M2U2IiwiaWF0IjoxNzE2NjM5ODY3fQ.10427Pg37n_IEeo41t5OJVsb5VgM8CMMJBa14v7ZC")
                .post(RequestBody.create(json, mediaType))
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //runOnUiThread(() -> Toast.makeText(ChatbotActivity2.this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    ChatbotResponse chatbotResponse = gson.fromJson(responseBody, ChatbotResponse.class);
                    if(!chatbotResponse.getData().getResponse().isEmpty()){ // Response 이 존재 -> 일반 질문
                        runOnUiThread(() -> addToChat(chatbotResponse.getData().getResponse(), Message.SENT_BY_BOT));
                    }
                    else if(chatbotResponse.getData().getCampus().isEmpty()){ // Campus 가 빈 문자열 -> 자신의 신고 조회
                        String url = chatbotResponse.getData().getUri();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    else { // 나머지 -> 시설물(강의실) 조회 & 신고
                        String url = chatbotResponse.getData().getUri();
//                        String campus = chatbotResponse.getData().getCampus();
//                        String building = chatbotResponse.getData().getBuilding();
//                        String classroom = chatbotResponse.getData().getClassroom();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ChatbotActivity2.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}