package com.skku.fixskku.facility.controller;

import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.common.domain.FacilityType;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.dto.res.FacilityResDto;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.facility.service.FacilityService;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback
class FacilityControllerTest {

    @Autowired
    FacilityController facilityController;

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    FacilityService facilityService;

    @BeforeEach
    void setUp() {
        // reports 테이블의 레코드를 먼저 삭제합니다.
        reportRepository.deleteAll();

        // facilities 테이블의 레코드를 삭제합니다.
        facilityRepository.deleteAll();

        Facility facility1 = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(101)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();
        Facility facility2 = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(101)
                .type(FacilityType.DESK)
                .status(FacilityStatus.UNUSABLE)
                .build();
        facilityRepository.save(facility1);
        facilityRepository.save(facility2);
    }

    @Test
    void 상태에따른_시설물_조회() {
        // when
        ResponseEntity<?> responseEntity = facilityController.getFacilitiesByStatus(101L, "validToken");

        // then
        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        List<FacilityResDto> facilityResDtos = (List<FacilityResDto>) ((ResponseApi.Body<?>) responseEntity.getBody()).getData();
        Assertions.assertNotNull(facilityResDtos);
        Assertions.assertEquals(2, facilityResDtos.size());
        Assertions.assertEquals("정상", facilityResDtos.get(0).getFacilityStatus());
        Assertions.assertEquals("고장", facilityResDtos.get(1).getFacilityStatus());
    }

    @Test
    void 토큰없는_시설물_조회_실패() {
        // when
        ResponseEntity<?> responseEntity = facilityController.getFacilitiesByStatus(101L, null);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCodeValue());
    }
}
