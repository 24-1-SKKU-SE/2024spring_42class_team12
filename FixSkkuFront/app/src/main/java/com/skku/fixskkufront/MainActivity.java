package com.skku.fixskkufront;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    ImageButton backButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        //llm.setStackFromEnd(true); //메세지가 밑에서부터 쌓이게 하는 코드
        recyclerView.setLayoutManager(llm);

        /* 아이콘 넣으려고 했으나, 실패했습니다. ㅠ
        * 첫 메세지 */
        addToChat("안녕하세요! 픽스꾸 봇 입니다! 무엇을 도와드릴까요?",Message.SENT_BY_BOT);

        /* 메세지 전송 */
        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME); // 메세지 보내기
            messageEditText.setText("");
            //welcomeTextView.setVisibility(View.GONE);
            imageView.setAlpha(0.2f);  // 투명도를 20%로 설정

        });
        /* 뒤로가기 */
        backButton.setOnClickListener((v)->{
            addToChat("Temp Answer...",Message.SENT_BY_BOT); // 메세지 답변
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


}