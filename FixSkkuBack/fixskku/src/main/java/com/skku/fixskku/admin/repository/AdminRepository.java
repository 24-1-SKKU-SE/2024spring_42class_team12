package com.skku.fixskku.admin.repository;

import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AdminRepository extends JpaRepository<Report, Long> {
    Page<Report> findByStatusAndCreationDateBetweenAndDescriptionContainingIgnoreCase(
            ReportStatus status, LocalDateTime startDate, LocalDateTime endDate, String description, Pageable pageable);

    Page<Report> findByStatusAndCreationDateBetween(
            ReportStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Report> findByStatusAndCreationDateAfter(
            ReportStatus status, LocalDateTime startDate, Pageable pageable);

    Page<Report> findByStatusAndCreationDateAfterAndDescriptionContainingIgnoreCase(
            ReportStatus status, LocalDateTime startDate, String description, Pageable pageable);

    Page<Report> findByStatusAndCreationDateBefore(
            ReportStatus status, LocalDateTime endDate, Pageable pageable);

    Page<Report> findByStatusAndCreationDateBeforeAndDescriptionContainingIgnoreCase(
            ReportStatus status, LocalDateTime endDate, String description, Pageable pageable);

    Page<Report> findByStatusAndDescriptionContainingIgnoreCase(
            ReportStatus status, String description, Pageable pageable);

    Page<Report> findByStatus(ReportStatus status, Pageable pageable);

    Page<Report> findByCreationDateBetweenAndDescriptionContainingIgnoreCase(
            LocalDateTime startDate, LocalDateTime endDate, String description, Pageable pageable);

    Page<Report> findByCreationDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Report> findByCreationDateAfter(
            LocalDateTime startDate, Pageable pageable);

    Page<Report> findByCreationDateAfterAndDescriptionContainingIgnoreCase(
            LocalDateTime startDate, String description, Pageable pageable);

    Page<Report> findByCreationDateBefore(
            LocalDateTime endDate, Pageable pageable);

    Page<Report> findByCreationDateBeforeAndDescriptionContainingIgnoreCase(
            LocalDateTime endDate, String description, Pageable pageable);

    Page<Report> findByDescriptionContainingIgnoreCase(
            String description, Pageable pageable);
}
