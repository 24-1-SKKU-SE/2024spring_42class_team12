package com.skku.fixskku.chatbot.service;

import com.skku.fixskku.chatbot.constants.ChatbotUrl;
import com.skku.fixskku.chatbot.dto.res.ChatbotResDto;
import com.skku.fixskku.chatbot.dto.res.ToChatbotReqDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotServiceImpl implements ChatbotService{
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 챗봇 FAQ 기능
     * @param tokenId 사용자의 토큰 아이디
     * @param faqId 사용자가 요청한 faq 아이디
     * @param text faq 응답 내용
     */
    @Override
    public void faqRequest(String tokenId, long faqId, String text) {
        ToChatbotReqDto reqDto = new ToChatbotReqDto(text, faqId+"");
        // 요청에 추가할 헤더 설정
        HttpHeaders headers = setHttpHeaders(tokenId);
        // 요청 객체 생성
        HttpEntity<ToChatbotReqDto> request = new HttpEntity<>(reqDto, headers);
        restTemplate.postForEntity(ChatbotUrl.CHATBOT_SERVER, request, ChatbotResDto.class);
    }

    /**
     * 챗봇 서버에게 요청 시 필요한 헤더를 설정하는 메서드
     * @param tokenId 사용자의 토큰 아이디
     * @return 완성된 HTTP 헤더
     */
    private static HttpHeaders setHttpHeaders(String tokenId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("ngrok-skip-browser-warning", "69420");
        headers.add("accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("token", tokenId);
        return headers;
    }
}
