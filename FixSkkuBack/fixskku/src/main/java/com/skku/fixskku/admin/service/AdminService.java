package com.skku.fixskku.admin.service;

import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AdminService {
    Page<ReportListResDto> getReports(ReportStatus reportStatus, LocalDate startDate, LocalDate endDate, String searchWord, Pageable pageable);
    ReportListResDto getReportById(Long reportId);
}
