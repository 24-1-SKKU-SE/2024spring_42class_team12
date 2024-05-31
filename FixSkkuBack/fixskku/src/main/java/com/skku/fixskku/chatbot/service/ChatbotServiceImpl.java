package com.skku.fixskku.chatbot.service;

import com.skku.fixskku.chatbot.constants.ChatbotUrl;
import com.skku.fixskku.chatbot.dto.res.ChatbotResDto;
import com.skku.fixskku.chatbot.dto.req.ToChatbotReqDto;
import com.skku.fixskku.chatbot.dto.res.ToFrontResDto;
import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

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
     * FAQ를 제외한 모든 챗봇 기능 요청을 챗봇으로 전달하고 응답을 API 명세에 맞게 가져오는 기능
     * @param tokenId 사용자의 토큰 아이디
     * @param text 사용자의 입력 텍스트
     * @return API 명세에 따른 응답 객체
     */
    @Override
    public ResponseEntity<?> sendToChatbotAndGetAnswer(String tokenId, String text) {
        // 텍스트를 챗봇에게 전달
        ToChatbotReqDto reqDto = new ToChatbotReqDto(text, null);
        // 요청에 추가할 헤더 설정
        HttpHeaders headers = setHttpHeaders(tokenId);
        // 요청 객체 생성
        HttpEntity<ToChatbotReqDto> request = new HttpEntity<>(reqDto, headers);
        ResponseEntity<ChatbotResDto> chatbotResponse = restTemplate.postForEntity(ChatbotUrl.CHATBOT_SERVER, request, ChatbotResDto.class);
        switch (Objects.requireNonNull(chatbotResponse.getBody()).getAnswerType()){
            // 챗봇 일반 질문 기능
            case NORMAL -> {
                return returnNormalQuestion(chatbotResponse.getBody());
            }
            // 챗봇 시설물 조회 기능
            case FAC -> {
                return returnFacilityInfo(chatbotResponse.getBody());
            }
            // 챗봇 신고 기능
            case REPORT -> {
                return returnReportForm(chatbotResponse.getBody());
            }
            // 챗봇 자신의 신고 조회 기능
            case MYREPORT -> {
                return returnMyReportForm(chatbotResponse.getBody());
            }
        }
        throw new IllegalStateException();
    }

    /**
     * 챗봇 일반 질문 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    public ResponseEntity<?> returnNormalQuestion(ChatbotResDto dto){
        ToFrontResDto resDto = new ToFrontResDto(
                dto.getText(), null, null, null, null);
        return ResponseApi.of(ResponseStatus._CHATBOT_NORMAL_SUCCESS,resDto);
    }

    /**
     * 챗봇 시설물 조회 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    public ResponseEntity<?> returnFacilityInfo(ChatbotResDto dto){
        ToFrontResDto resDto = new ToFrontResDto(
                null,
                ChatbotUrl.FACILITY_INFO_URI,
                dto.getData().getCampus(),
                dto.getData().getBuilding(),
                dto.getData().getClassroom());
        return ResponseApi.of(ResponseStatus._CHATBOT_FAC_SUCCESS,resDto);
    }

    /**
     * 챗봇 신고 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    public ResponseEntity<?> returnReportForm(ChatbotResDto dto){
        ToFrontResDto resDto = new ToFrontResDto(
                null,
                ChatbotUrl.REPORT_URI,
                dto.getData().getCampus(),
                dto.getData().getBuilding(),
                dto.getData().getClassroom());
        return ResponseApi.of(ResponseStatus._CHATBOT_FAC_SUCCESS,resDto);
    }

    /**
     * 챗봇 자신의 신고 조회 기능
     * @param dto 챗봇으로부터 받은 응답 DTO
     * @return 챗봇의 응답 DTO를 API 명세서에 맞게 바꾼 객체
     */
    public ResponseEntity<?> returnMyReportForm(ChatbotResDto dto){
        ToFrontResDto resDto = new ToFrontResDto(
                null,
                ChatbotUrl.MYREPORT_URI,
                null, null, null);
        return ResponseApi.of(ResponseStatus._CHATBOT_FAC_SUCCESS,resDto);
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
