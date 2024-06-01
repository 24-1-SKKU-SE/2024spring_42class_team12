package com.skku.fixskku.report.service;

import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.common.domain.FacilityType;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

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
    void 신고하기() {
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

    @Test
    void 자신의_신고_조회(){
        //given
        Random random = new Random();
        String token = "token1";

        //when
        Page<ReportListResDto> reports = reportService.getAllReports(token);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Report> findReports = repository.findAllByTokenId(token, pageable);
        int reportsSize = reports.getContent().size();
        int findReportsSize = findReports.getContent().size();
        int randInt = random.nextInt(reportsSize);
        System.out.println("reportsSize = " + reportsSize);
        System.out.println("findReportsSize = " + findReportsSize);
        System.out.println("randInt = " + randInt);

        ///then
        Assertions.assertEquals(findReportsSize, reportsSize);
        Assertions.assertEquals(
                reports.getContent().get(randInt).getReportId(),
                findReports.getContent().get(randInt).getId());
    }

    @Test
    void 자신의_신고_상세조회(){
        //given
        String token = "token1";

        long reportId = reportService.getAllReports(token).getContent().get(0).getReportId();
        ReportListResDto report = reportService.getReport(reportId, token);
        Report expectedReport = repository.findById(reportId).get();

        ///then
        Assertions.assertEquals(expectedReport.getId(), report.getReportId());
    }
}