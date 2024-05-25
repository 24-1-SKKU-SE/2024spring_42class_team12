package com.skku.fixskku.report.service;

import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import com.skku.fixskku.report.domain.Report;
import com.skku.fixskku.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
