package com.skku.fixskku.admin.service;

import com.skku.fixskku.admin.dto.req.ReportUpdateDto;
import com.skku.fixskku.admin.repository.AdminRepository;
import com.skku.fixskku.common.apipayload.ResponseStatus;
import com.skku.fixskku.common.apipayload.exception.GeneralException;
import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.repository.ReportRepository;
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

    @Autowired
    private ReportRepository reportRepository;

    @Override
    @Transactional(readOnly = true)
    public ReportListResDto getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ResponseStatus._ADMIN_NOT_FOUND));
        return new ReportListResDto(report);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<ReportListResDto> getReports(ReportStatus reportStatus, LocalDate startDate, LocalDate endDate, String searchWord, Pageable pageable) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        log.debug("ReportStatus: {}, StartDate: {}, EndDate: {}, SearchWord: {}", reportStatus, startDateTime, endDateTime, searchWord);

        Page<Report> reports;
        if (reportStatus != null) {
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
        } else {
            if (startDateTime != null && endDateTime != null) {
                if (searchWord != null && !searchWord.trim().isEmpty()) {
                    reports = adminRepository.findByCreationDateBetweenAndDescriptionContainingIgnoreCase(
                            startDateTime, endDateTime, searchWord, pageable);
                } else {
                    reports = adminRepository.findByCreationDateBetween(
                            startDateTime, endDateTime, pageable);
                }
            } else if (startDateTime != null) {
                if (searchWord != null && !searchWord.trim().isEmpty()) {
                    reports = adminRepository.findByCreationDateAfterAndDescriptionContainingIgnoreCase(
                            startDateTime, searchWord, pageable);
                } else {
                    reports = adminRepository.findByCreationDateAfter(
                            startDateTime, pageable);
                }
            } else if (endDateTime != null) {
                if (searchWord != null && !searchWord.trim().isEmpty()) {
                    reports = adminRepository.findByCreationDateBeforeAndDescriptionContainingIgnoreCase(
                            endDateTime, searchWord, pageable);
                } else {
                    reports = adminRepository.findByCreationDateBefore(
                            endDateTime, pageable);
                }
            } else {
                if (searchWord != null && !searchWord.trim().isEmpty()) {
                    reports = adminRepository.findByDescriptionContainingIgnoreCase(
                            searchWord, pageable);
                } else {
                    reports = adminRepository.findAll(pageable);
                }
            }
        }

        return reports.map(ReportListResDto::new);
    }
    @Override
    @Transactional
    public ReportListResDto updateReport(Long reportId, ReportUpdateDto updateDto) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new GeneralException(ResponseStatus._ADMIN_NOT_FOUND));

        if (updateDto.getReportStatus() != null) {
            report.setStatus(ReportStatus.valueOf(updateDto.getReportStatus()));
        }
        if (updateDto.getRejectionReason() != null) {
            report.setRejectionReason(updateDto.getRejectionReason());
        }

        Report updatedReport = reportRepository.save(report);
        return new ReportListResDto(updatedReport);
    }
}
