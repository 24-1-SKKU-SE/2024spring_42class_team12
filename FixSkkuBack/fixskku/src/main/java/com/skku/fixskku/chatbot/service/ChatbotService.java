package com.skku.fixskku.chatbot.service;

import org.springframework.stereotype.Service;

@Service
public interface ChatbotService {

    /**
     * 챗봇 FAQ 기능
     * @param tokenId 사용자의 토큰 아이디
     * @param faqId 사용자가 요청한 faq 아이디
     * @param text faq 응답 내용
     */
    void faqRequest(String tokenId, long faqId, String text);
}
