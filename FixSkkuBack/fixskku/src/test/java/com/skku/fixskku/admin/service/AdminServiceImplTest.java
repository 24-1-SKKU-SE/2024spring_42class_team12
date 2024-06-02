package com.skku.fixskku.admin.service;

import com.skku.fixskku.admin.dto.req.ReportUpdateDto;
import com.skku.fixskku.admin.repository.AdminRepository;
import com.skku.fixskku.common.domain.*;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Rollback
class AdminServiceImplTest {

    @Autowired
    AdminServiceImpl adminService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    FacilityRepository facilityRepository;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        facilityRepository.deleteAll();
    }

    @Test
    void getReportById() {
        // given
        Facility facility = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();
        facilityRepository.save(facility);

        Report report = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .build();
        reportRepository.save(report);

        // when
        ReportListResDto reportDto = adminService.getReportById(report.getId());

        // then
        Assertions.assertEquals(report.getId(), reportDto.getReportId());
    }

    @Test
    void getReports() {
        // given
        Facility facility = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();
        facilityRepository.save(facility);

        Report report1 = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .description("Report 1")
                .build();
        Report report2 = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .description("Report 2")
                .build();
        reportRepository.save(report1);
        reportRepository.save(report2);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ReportListResDto> reports = adminService.getReports(ReportStatus.REPORT_RECEIVED, null, null, null, pageable);

        // then
        Assertions.assertEquals(2, reports.getTotalElements());
    }

    @Test
    void updateReport() {
        // given
        Facility facility = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();
        facilityRepository.save(facility);

        Report report = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .build();
        reportRepository.save(report);

        ReportUpdateDto updateDto = new ReportUpdateDto();
        updateDto.setReportId(report.getId());
        updateDto.setFacilityId(facility.getId());
        updateDto.setReportStatus(ReportStatus.REPAIR_COMPLETED.name()); // enum 상수 사용
        updateDto.setRejectionReason("Reason for rejection");

        // when
        ReportListResDto updatedReportDto = adminService.updateReport(report.getId(), updateDto);

        // then
        Assertions.assertEquals(ReportStatus.REPAIR_COMPLETED.getName(), updatedReportDto.getReportStatus()); // 한글 이름으로 검증
        Assertions.assertEquals("Reason for rejection", updatedReportDto.getRejectionReason());
    }

    @Test
    void getReportsWithFilter() {
        // given
        Facility facility = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();
        facilityRepository.save(facility);

        Report report1 = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .description("Report 1")
                .build();
        Report report2 = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .description("Report 2")
                .build();
        reportRepository.save(report1);
        reportRepository.save(report2);

        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        String searchWord = "Report";

        // when
        Page<ReportListResDto> reports = adminService.getReports(ReportStatus.REPORT_RECEIVED, startDate, endDate, searchWord, pageable);

        // then
        Assertions.assertEquals(2, reports.getTotalElements());
    }
}

