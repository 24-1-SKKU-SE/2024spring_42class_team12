package com.skku.fixskku.chatbot.service;

import com.skku.fixskku.chatbot.dto.res.ChatbotResDto;
import org.springframework.http.ResponseEntity;
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

    /**
     * FAQ를 제외한 모든 챗봇 기능 요청을 챗봇으로 전달하고 응답을 API 명세에 맞게 가져오는 기능
     * @param tokenId 사용자의 토큰 아이디
     * @param text 사용자의 입력 텍스트
     * @return API 명세에 따른 응답 객체
     */
    ResponseEntity<?> sendToChatbotAndGetAnswer(String tokenId, String text);

    /**
     * 챗봇 일반 질문 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    ResponseEntity<?> returnNormalQuestion(ChatbotResDto dto);

    /**
     * 챗봇 시설물 조회 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    ResponseEntity<?> returnFacilityInfo(ChatbotResDto dto);

    /**
     * 챗봇 신고 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    ResponseEntity<?> returnReportForm(ChatbotResDto dto);
}
