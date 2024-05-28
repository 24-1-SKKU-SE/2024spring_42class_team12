package com.skku.fixskku.report.service;

import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    /**
     * 신고하는 기능
     * @param report 신고 객체
     */
    long doReport(Report report);

    /**
     * 자신의 신고를 조회하는 기능
     * @param token 조회할 사용자의 토큰 아이디
     * @return 응답 형식에 맞게 페이징된 사용자의 신고 목록 리스트
     */
    Page<ReportListResDto> getAllReports(String token);
}
