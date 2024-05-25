package com.skku.fixskku.facility.dto;

import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "facilityid")
    private long id;

    @Column(name = "campus")
    @Enumerated(EnumType.STRING)
    private CampusType campusType;

    @Column(name = "building")
    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;

    @Column(name = "floor")
    private String floor;

    @Column(name = "classroom")
    private long classroomNumber;

    @Column(name = "facilitytype")
    @Enumerated(EnumType.STRING)
    private FacilityType type;

    @Column(name = "facilitystatus")
    @Enumerated(EnumType.STRING)
    private FacilityStatus status;

}
