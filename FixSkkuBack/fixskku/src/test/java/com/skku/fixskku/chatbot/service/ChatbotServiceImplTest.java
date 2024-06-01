package com.skku.fixskku.chatbot.service;

import com.skku.fixskku.chatbot.dto.res.ToFrontResDto;
import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
@Rollback
class ChatbotServiceImplTest {

    @Autowired
    ChatbotService chatbotService;

    @Autowired
    ReportRepository repository;

    ResponseEntity<ResponseApi.Body<ToFrontResDto>> responseEntity;

    @Test
    public void 챗봇을_통한_FAQ_질문_경우() throws Exception{
        //given
        String token = "token1";
        String text = "사용법을 알려줘";

        //when
        responseEntity = (ResponseEntity<ResponseApi.Body<ToFrontResDto>>) chatbotService.sendToChatbotAndGetAnswer(token, text);
        int code = responseEntity.getBody().getCode();
        String message = responseEntity.getBody().getMessage();
        ToFrontResDto data = responseEntity.getBody().getData();
        String response = data.getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), code);
        assertEquals("챗봇 일반 질문 응답 성공", message);
        assertNotNull(response);
    }

    @Test
    public void 챗봇을_통한_일반_질문_경우() throws Exception{
        //given
        String token = "token1";
        String text = "사용법을 알려줘";

        //when
        responseEntity = (ResponseEntity<ResponseApi.Body<ToFrontResDto>>) chatbotService.sendToChatbotAndGetAnswer(token, text);
        int code = responseEntity.getBody().getCode();
        String message = responseEntity.getBody().getMessage();
        ToFrontResDto data = responseEntity.getBody().getData();
        String response = data.getResponse();

        //then
        assertEquals(HttpStatus.OK.value(), code);
        assertEquals("챗봇 일반 질문 응답 성공", message);
        assertNotNull(response);
    }

    @Test
    public void 챗봇을_통한_신고_경우() throws Exception{
        //given
        String token = "token1";
        String text = "자연과학캠퍼스 수성관 05102 시설물 신고";

        //when
        responseEntity = (ResponseEntity<ResponseApi.Body<ToFrontResDto>>) chatbotService.sendToChatbotAndGetAnswer(token, text);
        int code = responseEntity.getBody().getCode();
        String message = responseEntity.getBody().getMessage();
        ToFrontResDto data = responseEntity.getBody().getData();
        String uri = data.getUri();
        String campus = data.getCampus();
        String building = data.getBuilding();
        String classroom = data.getClassroom();

        //then
        assertEquals(HttpStatus.CREATED.value(), code);
        assertEquals("챗봇 신고 요청 성공", message);
        assertNotNull(uri);
        assertEquals(CampusType.SUWON_CAMPUS.getCampusName(), campus);
        assertEquals(BuildingType.SUSUNG.getName(), building);
        assertEquals("05102", classroom);

    }

    @Test
    public void 챗봇을_통한_자신의_신고_조회_경우() throws Exception{
        //given
        String token = "token1";
        String text = "내 신고 조회";

        //when
        responseEntity = (ResponseEntity<ResponseApi.Body<ToFrontResDto>>) chatbotService.sendToChatbotAndGetAnswer(token, text);
        int code = responseEntity.getBody().getCode();
        String message = responseEntity.getBody().getMessage();
        ToFrontResDto data = responseEntity.getBody().getData();
        String uri = data.getUri();

        //then
        assertEquals(HttpStatus.OK.value(), code);
        assertEquals("챗봇 자신의 신고 조회 성공", message);
        assertNotNull(uri);
    }

    @Test
    public void 챗봇을_통한_강의실_정보_조회_경우() throws Exception{
        //given
        String token = "token1";
        String text = "인문사회과학캠퍼스 경영관 33404  강의실 정보 조회";

        //when
        responseEntity = (ResponseEntity<ResponseApi.Body<ToFrontResDto>>) chatbotService.sendToChatbotAndGetAnswer(token, text);

        int code = responseEntity.getBody().getCode();
        String message = responseEntity.getBody().getMessage();
        ToFrontResDto data = responseEntity.getBody().getData();
        String uri = data.getUri();
        String campus = data.getCampus();
        String building = data.getBuilding();
        String classroom = data.getClassroom();

        //then
        assertEquals(HttpStatus.OK.value(), code);
        assertEquals("챗봇 망가진 시설물 정보 조회 성공", message);
        assertNotNull(uri);
        assertEquals(CampusType.SEOUL_CAMPUS.getCampusName(), campus);
        assertEquals(BuildingType.BUS.getName(), building);
        assertEquals("33404", classroom);
    }
}