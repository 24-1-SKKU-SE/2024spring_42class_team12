package com.skku.fixskku.chatbot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@Rollback
class ChatbotControllerTest {

    @Autowired
    ChatbotController chatbotController;

    String token = "token1";
    @Test
    void 챗봇_전체_기능() {
        //faq
        String text = "사용법을 알려줘";
        ResponseEntity<?> textFromUser = chatbotController.getTextFromUser(token, 1L, text);
        int value = textFromUser.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);

        // 일반 질문
        text = "사용법을 알려줘";
        textFromUser = chatbotController.getTextFromUser(token, null, text);
        value = textFromUser.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);

        // 신고
        text = "자연과학캠퍼스 수성관 05102 시설물 신고";
        textFromUser = chatbotController.getTextFromUser(token, null, text);
        value = textFromUser.getStatusCode().value();
        assertEquals(HttpStatus.CREATED.value(), value);

        // 자신의 신고 조회
        text = "내 신고 조회";
        textFromUser = chatbotController.getTextFromUser(token, null, text);
        value = textFromUser.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);

        // 강의실 정보 조회
        text = "인문사회과학캠퍼스 경영관 33404  강의실 정보 조회";
        textFromUser = chatbotController.getTextFromUser(token, null, text);
        value = textFromUser.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);
    }
}