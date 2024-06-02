package com.skku.fixskku.facility.service;

import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.common.domain.FacilityType;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback
class FacilityServiceTest {

    @Autowired
    FacilityService facilityService;

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        // reports 테이블의 레코드를 먼저 삭제합니다.
        reportRepository.deleteAll();

        // facilities 테이블의 레코드를 삭제합니다.
        facilityRepository.deleteAll();
    }

    @Test
    void findFacilitiesByStatus() {
        // given
        Facility facility1 = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();

        Facility facility2 = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.DESK)
                .status(FacilityStatus.UNUSABLE)
                .build();

        facilityRepository.save(facility1);
        facilityRepository.save(facility2);

        // when
        List<Facility> facilities = facilityService.findFacilitiesByStatus(22022L);

        // then
        Assertions.assertEquals(2, facilities.size());
        Assertions.assertTrue(facilities.contains(facility1));
        Assertions.assertTrue(facilities.contains(facility2));
    }
}
