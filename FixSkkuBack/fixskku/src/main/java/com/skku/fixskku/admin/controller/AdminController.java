package com.skku.fixskku.admin.controller;

import com.skku.fixskku.admin.service.AdminService;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.common.apipayload.ResponseApi;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    public ResponseEntity<?> getReports(
            @RequestParam(required = false) ReportStatus reportStatus,  // Enum 타입으로 직접 받음
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String searchWord,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        try {
            LocalDate start = null;
            LocalDate end = null;
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

            if (startDate != null && !startDate.trim().isEmpty()) {
                start = LocalDate.parse(startDate.trim(), formatter);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                end = LocalDate.parse(endDate.trim(), formatter);
            }

            Page<ReportListResDto> reports = adminService.getReports(reportStatus, start, end, searchWord, pageable);

            Map<String, Object> data = new HashMap<>();
            data.put("page", reports.getNumber() + 1);
            data.put("totalPages", reports.getTotalPages());
            data.put("reports", reports.getContent());

            return ResponseApi.of(ResponseStatus._REPORT_LIST_SUCCESS, data);
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            return ResponseApi.badRequest();
        }
    }
}
