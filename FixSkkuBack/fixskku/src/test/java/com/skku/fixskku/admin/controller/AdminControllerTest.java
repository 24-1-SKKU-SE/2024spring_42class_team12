package com.skku.fixskku.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.fixskku.admin.dto.req.ReportUpdateDto;
import com.skku.fixskku.common.domain.*;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Facility facility;
    private Report report;

    @BeforeEach
    void setup() {
        facility = Facility.builder()
                .campusType(CampusType.SEOUL_CAMPUS)
                .buildingType(BuildingType.BUS)
                .floor("1")
                .classroomNumber(22022L)
                .type(FacilityType.CHAIR)
                .status(FacilityStatus.USABLE)
                .build();
        facilityRepository.save(facility);

        report = Report.builder()
                .facility(facility)
                .tokenId("token")
                .creationDate(LocalDateTime.now())
                .status(ReportStatus.REPORT_RECEIVED)
                .build();
        reportRepository.save(report);
    }

    @Test
    void getReports() throws Exception {
        mockMvc.perform(get("/admin/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("신고가 성공적으로 조회되었습니다."))
                .andExpect(jsonPath("$.data.reports").isNotEmpty());
    }

    @Test
    void getReportById() throws Exception {
        mockMvc.perform(get("/admin/" + report.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("신고가 성공적으로 조회되었습니다."))
                .andExpect(jsonPath("$.data.report.reportId").value(report.getId()));
    }

    @Test
    void updateReport() throws Exception {
        ReportUpdateDto updateDto = new ReportUpdateDto();
        updateDto.setReportStatus("REJECTED");
        updateDto.setRejectionReason("수리 불필요");

        mockMvc.perform(patch("/admin/" + report.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("신고 관리 성공"))
                .andExpect(jsonPath("$.data.report.reportStatus").value("반려"))
                .andExpect(jsonPath("$.data.report.rejectionReason").value("수리 불필요"));
    }
}
