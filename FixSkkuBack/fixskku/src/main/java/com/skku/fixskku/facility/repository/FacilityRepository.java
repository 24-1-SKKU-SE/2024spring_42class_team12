package com.skku.fixskku.facility.repository;

import com.skku.fixskku.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // 특정 강의실의 ID와 특정 상태들을 가진 시설물을 조회하는 메소드
    List<Facility> findByClassroomAndFacilityStatusIn(Long classroom, List<String> statuses);
}
