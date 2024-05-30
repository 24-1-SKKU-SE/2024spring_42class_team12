package com.skku.fixskku.report.repository;

import com.skku.fixskku.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 자신의 모든 신고를 조회
     * @param token 사용자의 토큰 아이디
     * @return 페이지 정보를 포함한 해당 사용자의 모든 신고 리스트
     */
    Page<Report> findAllByTokenId(String token, Pageable pageable);

    /**
     * 자신의 신고를 상세 조회
     * @param id 신고 아이디
     * @param token 사용자의 토큰 아이디
     * @return 사용자의 해당 신고 객체
     */
    Report findByIdAndTokenId(long id, String token);
}
