package com.skku.fixskku.report.service;

import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    ReportRepository repository;
    /**
     * 신고하는 기능
     * @param report 신고 객체
     */
    @Override
    public void doReport(Report report) {
        repository.save(report);
    }
}
