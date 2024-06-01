package com.skku.fixskku.report.controller;

import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityType;
import com.skku.fixskku.report.dto.req.ReportReqDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Rollback
class ReportControllerTest {

    @Autowired
    ReportController reportController;
    @Test
    void 신고하기_통합() {
        ResponseEntity<?> responseEntity = reportController.doReport(
                new ReportReqDto(
                        "token",
                        CampusType.SEOUL_CAMPUS,
                        BuildingType.BUS,
                        "1",
                        33101,
                        FacilityType.DESK,
                        1,
                        "설명",
                        "사진"));
        int value = responseEntity.getStatusCode().value();
        assertEquals(HttpStatus.CREATED.value(), value);
    }

    @Test
    void 자신의_모든_신고_조회_통합() {
        ResponseEntity<?> responseEntity = reportController.getAllReports("token");
        int value = responseEntity.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);

    }

    @Test
    void 자신의_신고_상세_조회() {
        ResponseEntity<?> responseEntity = reportController.getAllReports("token1", 1L);
        int value = responseEntity.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), value);
    }
}