package com.skku.fixskku.facility.service;

import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.facility.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<Facility> findFacilitiesByStatus(Long classroom) {
        return facilityRepository.findByClassroomNumberAndStatusIn(classroom, ReportStatus.getStatusNames());
    }
}
