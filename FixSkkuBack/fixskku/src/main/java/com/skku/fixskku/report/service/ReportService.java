package com.skku.fixskku.report.service;

import com.skku.fixskku.report.domain.Report;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    /**
     * 신고하는 기능
     * @param report 신고 객체
     */
    long doReport(Report report);
}
