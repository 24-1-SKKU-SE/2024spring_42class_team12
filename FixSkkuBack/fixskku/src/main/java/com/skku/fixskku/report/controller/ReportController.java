package com.skku.fixskku.report.controller;

import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import com.skku.fixskku.common.apipayload.exception.GeneralException;
import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.dto.req.ReportReqDto;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.report.dto.res.ReportResDto;
import com.skku.fixskku.report.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {
    private final ReportService reportService;
    private final FacilityRepository facilityRepository;

    public ReportController(ReportService reportService, FacilityRepository facilityRepository) {
        this.reportService = reportService;
        this.facilityRepository = facilityRepository;
    }

    /**
     * 신고기능
     * @param requestDto 신고에 필요한 요청 객체
     * @return API 명세서에 따른 응답
     */
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

    /**
     * 자신의 신고 조회 기능
     * @param token 조회할 사용자의 토큰 아이디
     * @return API 명세서에 따른 응답
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllReports(@RequestParam String token){
        if (token== null || token.isEmpty()) {
            return ResponseApi.badRequest();
        }
        try {
            Page<ReportListResDto> resultReports = reportService.getAllReports(token);
            List<ReportListResDto> reports = resultReports.getContent();
            Map<String, Object> result = new HashMap<>();
            result.put("reports", reports);
            result.put("page", resultReports.getNumber() + 1);
            result.put("totalPages", resultReports.getTotalPages());
            return ResponseApi.of(ResponseStatus._REPORT_LIST_SUCCESS, result);
        }catch (GeneralException e) {
            return ResponseApi.of(e.getStatus());
        }catch (NoSuchElementException | NullPointerException e){
            return ResponseApi.badRequest();
        }
        catch (Exception e) {
            return ResponseApi.serverError();
        }
    }

    /**
     * 자신의 신고 상세조회 기능
     * @param token 조회할 사용자의 토큰 아이디
     * @return API 명세서에 따른 응답
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getAllReports(@RequestParam String token, @PathVariable long reportId){
        if (token== null || token.isEmpty()) {
            return ResponseApi.badRequest();
        }
        try {
            ReportListResDto  resultReport = reportService.getReport(reportId, token);
            Map<String, Object> result = new HashMap<>();
            result.put("report", resultReport);
            return ResponseApi.of(ResponseStatus._REPORT_LIST_SUCCESS, result);
        }catch (GeneralException e) {
            return ResponseApi.of(e.getStatus());
        } catch (NoSuchElementException | NullPointerException e){
            return ResponseApi.badRequest();
        }
        catch (Exception e) {
            return ResponseApi.serverError();
        }
    }

}
