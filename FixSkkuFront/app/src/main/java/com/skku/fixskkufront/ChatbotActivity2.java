package com.skku.fixskkufront;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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


        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        intent = getIntent();
        String FAQ = intent.getStringExtra(ChatbotActivity.EXT_FAQ);
        /* 만약 FAQ 가 1이라면 FAQ1에 대한 답변을 생성함. */
        if (FAQ != null && FAQ.equals("1")) {
            addToChat("(FAQ1 에 대한 답변)",Message.SENT_BY_BOT);
        }

        //addToChat("안녕하세요! 픽스꾸 봇 입니다! 무엇을 도와드릴까요?",Message.SENT_BY_BOT);

        /* 메세지 전송 & 서버에도 메세지를 보내야함. */
        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME); // 메세지 보내기
            messageEditText.setText("");
            //welcomeTextView.setVisibility(View.GONE);
            sendMessageToServer(question);
            imageView.setAlpha(0.2f);  // 투명도를 20%로 설정

        });
        /* 뒤로가기 */
        backButton.setOnClickListener((v)->{
            //addToChat("Temp Answer...",Message.SENT_BY_BOT); // 메세지 답변
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

    void sendMessageToServer(String message) {
        // Create JSON object
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", "your-token");
        jsonObject.addProperty("message", message);
        String jsonString = gson.toJson(jsonObject);

        // Create request body
        RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));

        // Build request
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/testget")
                .post(body)
                .build();

        // Enqueue request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ChatbotActivity2.this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    ChatbotResponse chatbotResponse = gson.fromJson(responseBody, ChatbotResponse.class);
                    runOnUiThread(() -> addToChat(chatbotResponse.getData().getResponse(), Message.SENT_BY_BOT));
                } else {
                    runOnUiThread(() -> Toast.makeText(ChatbotActivity2.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}