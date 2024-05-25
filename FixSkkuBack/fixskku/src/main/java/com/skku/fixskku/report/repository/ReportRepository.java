package com.skku.fixskku.report.repository;

import com.skku.fixskku.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * 신고를 DB에 저장
     *
     * @param report 신고 객체
     * @return 저장한 객체
     */
    Report save(Report report);
}
