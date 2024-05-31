package com.skku.fixskku.admin.controller;

import com.skku.fixskku.admin.dto.req.ReportUpdateDto;
import com.skku.fixskku.admin.service.AdminService;
import com.skku.fixskku.common.apipayload.exception.GeneralException;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    public ResponseEntity<?> getReports(
            @RequestParam(required = false) String reportStatus,  // 한글로 받기 위해 String 타입으로 변경
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String searchWord,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        try {
            LocalDate start = null;
            LocalDate end = null;
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            ReportStatus status = null;

            if (reportStatus != null && !reportStatus.trim().isEmpty()) {
                status = ReportStatus.fromName(reportStatus.trim());
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                start = LocalDate.parse(startDate.trim(), formatter);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                end = LocalDate.parse(endDate.trim(), formatter);
            }

            Page<ReportListResDto> reports = adminService.getReports(status, start, end, searchWord, pageable);

            Map<String, Object> data = new HashMap<>();
            data.put("page", reports.getNumber() + 1);
            data.put("totalPages", reports.getTotalPages());
            data.put("reports", reports.getContent());

            return ResponseApi.of(ResponseStatus._REPORT_LIST_SUCCESS, data);
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            return ResponseApi.badRequest();
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable long reportId) {
        try {
            ReportListResDto report = adminService.getReportById(reportId);
            Map<String, Object> result = new HashMap<>();
            result.put("report", report);
            return ResponseApi.of(ResponseStatus._REPORT_LIST_SUCCESS, result);
        } catch (GeneralException e) {
            return ResponseApi.of(e.getStatus());
        } catch (NoSuchElementException e) {
            return ResponseApi.of(ResponseStatus._ADMIN_NOT_FOUND);
        } catch (Exception e) {
            return ResponseApi.serverError();
        }
    }

    @PatchMapping("/{reportId}")
    public ResponseEntity<?> updateReport(@PathVariable long reportId, @RequestBody ReportUpdateDto updateDto) {
        try {
            ReportListResDto updatedReport = adminService.updateReport(reportId, updateDto);
            Map<String, Object> result = new HashMap<>();
            result.put("report", updatedReport);
            return ResponseApi.of(ResponseStatus._REPORT_LIST_SUCCESS, result);
        } catch (GeneralException e) {
            return ResponseApi.of(e.getStatus());
        } catch (NoSuchElementException e) {
            return ResponseApi.of(ResponseStatus._ADMIN_NOT_FOUND);
        } catch (Exception e) {
            return ResponseApi.serverError();
        }
    }
}
