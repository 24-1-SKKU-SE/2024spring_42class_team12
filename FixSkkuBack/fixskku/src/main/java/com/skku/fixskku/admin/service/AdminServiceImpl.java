package com.skku.fixskku.admin.service;

import com.skku.fixskku.admin.repository.AdminRepository;
import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.report.domain.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ReportListResDto> getReports(ReportStatus reportStatus, LocalDate startDate, LocalDate endDate, String searchWord, Pageable pageable) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        log.debug("ReportStatus: {}, StartDate: {}, EndDate: {}, SearchWord: {}", reportStatus, startDateTime, endDateTime, searchWord);

        Page<Report> reports;
        if (startDateTime != null && endDateTime != null) {
            if (searchWord != null && !searchWord.trim().isEmpty()) {
                reports = adminRepository.findByStatusAndCreationDateBetweenAndDescriptionContainingIgnoreCase(
                        reportStatus, startDateTime, endDateTime, searchWord, pageable);
            } else {
                reports = adminRepository.findByStatusAndCreationDateBetween(
                        reportStatus, startDateTime, endDateTime, pageable);
            }
        } else if (startDateTime != null) {
            if (searchWord != null && !searchWord.trim().isEmpty()) {
                reports = adminRepository.findByStatusAndCreationDateAfterAndDescriptionContainingIgnoreCase(
                        reportStatus, startDateTime, searchWord, pageable);
            } else {
                reports = adminRepository.findByStatusAndCreationDateAfter(
                        reportStatus, startDateTime, pageable);
            }
        } else if (endDateTime != null) {
            if (searchWord != null && !searchWord.trim().isEmpty()) {
                reports = adminRepository.findByStatusAndCreationDateBeforeAndDescriptionContainingIgnoreCase(
                        reportStatus, endDateTime, searchWord, pageable);
            } else {
                reports = adminRepository.findByStatusAndCreationDateBefore(
                        reportStatus, endDateTime, pageable);
            }
        } else {
            if (searchWord != null && !searchWord.trim().isEmpty()) {
                reports = adminRepository.findByStatusAndDescriptionContainingIgnoreCase(
                        reportStatus, searchWord, pageable);
            } else {
                reports = adminRepository.findByStatus(
                        reportStatus, pageable);
            }
        }

        return reports.map(ReportListResDto::new);
    }
}
