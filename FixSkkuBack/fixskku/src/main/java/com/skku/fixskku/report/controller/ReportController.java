package com.skku.fixskku.report.controller;

import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import com.skku.fixskku.common.apipayload.exception.GeneralException;
import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.dto.req.ReportReqDto;
import com.skku.fixskku.report.dto.res.ReportResDto;
import com.skku.fixskku.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final FacilityRepository facilityRepository;

    public ReportController(ReportService reportService, FacilityRepository facilityRepository) {
        this.reportService = reportService;
        this.facilityRepository = facilityRepository;
    }
    @PostMapping
    public ResponseEntity<?> doReport(ReportReqDto requestDto){
        if (requestDto.getToken()== null || requestDto.getToken().isEmpty()) {
            return ResponseApi.badRequest();
        }
        try{
            Facility findFacility = facilityRepository.findById(requestDto.getFacilityId()).get();
            Report report = Report.builder()
                    .facility(findFacility)
                    .tokenId(requestDto.getToken())
                    .description(requestDto.getDescription())
                    .photoUrl(requestDto.getPhotoUrl())
                    .creationDate(LocalDateTime.now())
                    .status(ReportStatus.REPORT_RECEIVED)
                    .build();
            long saveReportId = reportService.doReport(report);
            return ResponseApi.of(ResponseStatus._REPORT_SUCCESS, new ReportResDto(saveReportId));
        }catch (GeneralException e) {
            return ResponseApi.of(e.getStatus());
        } catch (Exception e) {
            return ResponseApi.serverError();
        }
    }
}
