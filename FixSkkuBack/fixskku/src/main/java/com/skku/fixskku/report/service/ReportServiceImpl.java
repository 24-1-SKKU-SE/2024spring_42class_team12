package com.skku.fixskku.report.service;

import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.dto.res.ReportListResDto;
import com.skku.fixskku.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    ReportRepository repository;
    @Autowired
    FacilityRepository facilityRepository;
    /**
     * 신고하는 기능
     * @param report 신고 객체
     */
    @Override
    @Transactional
    public long doReport(Report report) {
        Report saveReport = repository.save(report);
        // 시설물의 상태 바꾸기
        Facility facility = facilityRepository.findById(report.getFacility().getId()).get();
        facility.setStatus(FacilityStatus.UNUSABLE);
        facilityRepository.save(facility);
        return saveReport.getId();
    }

    /**
     * 자신의 신고를 조회하는 기능
     * @param token 조회할 사용자의 토큰 아이디
     * @return 응답 형식에 맞게 페이징된 사용자의 신고 목록 리스트
     */
    @Override
    public Page<ReportListResDto> getAllReports(String token) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Report> findReports = repository.findAllByTokenId(token, pageable);
        // Pageable 객체에서 페이지 정보 추출
        return findReports.map(ReportListResDto::new);
    }
}
