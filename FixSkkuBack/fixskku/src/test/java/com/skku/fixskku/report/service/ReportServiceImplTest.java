package com.skku.fixskku.report.service;

import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.facility.dto.Facility;
import com.skku.fixskku.common.domain.FacilityType;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback
class ReportServiceImplTest {
    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository repository;
    @Autowired
    FacilityRepository facilityRepository;

    @Test
    void doReport() {
        // given
        Facility fac = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.UNUSABLE).build();
        facilityRepository.save(fac);

        Report report = Report.builder()
                .facility(fac)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .photoUrl("photo")
                .description("설명")
                .build();

        // when
        reportService.doReport(report);

        //then
        Optional<Report> findReport = repository.findById(report.getId());
        Assertions.assertEquals(report, findReport.get());
    }
}