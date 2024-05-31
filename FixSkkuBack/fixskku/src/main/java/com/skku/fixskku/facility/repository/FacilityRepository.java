package com.skku.fixskku.facility.repository;

import com.skku.fixskku.facility.domain.Facility;
import com.skku.fixskku.common.domain.FacilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByClassroomNumberAndStatusIn(long classroomNumber, List<FacilityStatus> statuses);
}
