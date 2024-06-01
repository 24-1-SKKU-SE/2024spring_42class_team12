package com.skku.fixskku.chatbot.controller;

import com.skku.fixskku.chatbot.service.ChatbotService;
import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import com.skku.fixskku.common.apipayload.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@Slf4j
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public ResponseEntity<?> getTextFromUser(@RequestHeader(name = "token")String tokenId,
                                             @RequestParam(name = "faq", required = false) Long faqId, String text){
        // 1. 챗봇을 통해 FAQ를 물어본 경우(프론트에서 FAQ 버튼을 클릭)
        if(faqId!=null){
            try {
                chatbotService.faqRequest(tokenId, faqId, text);
                return ResponseApi.of(ResponseStatus._CHATBOT_FAQ_SUCCESS,null);
            }catch (GeneralException e) {
                log.error(e.toString());
                return ResponseApi.of(e.getStatus());
            } catch (Exception e) {
                log.error(e.toString());
                return ResponseApi.serverError();
            }
        }
        // 2. 일반 질문, 신고, 자신의 신고 조회, 시설물 조회의 경우
        try {
            return chatbotService.sendToChatbotAndGetAnswer(tokenId, text);
        }catch (GeneralException e) {
            log.error(e.toString());
            return ResponseApi.of(e.getStatus());
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseApi.serverError();
        }
    }
}
