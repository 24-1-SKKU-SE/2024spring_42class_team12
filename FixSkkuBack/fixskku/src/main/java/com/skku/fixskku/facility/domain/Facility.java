package com.skku.fixskku.facility.domain;

import com.skku.fixskku.common.domain.FacilityStatus;
import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityType;
import com.skku.fixskku.report.domain.Report;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "facility_id")
    private long id;

    @OneToMany(mappedBy = "facility")
    private List<Report> reports;

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

    @Column(name = "facility_type")
    @Enumerated(EnumType.STRING)
    private FacilityType type;

    @Column(name = "facility_status")
    @Enumerated(EnumType.STRING)
    private FacilityStatus status;

    public Facility(CampusType campusType, BuildingType buildingType, String floor, long classroomNumber, FacilityType type, FacilityStatus status) {
        this.campusType = campusType;
        this.buildingType = buildingType;
        this.floor = floor;
        this.classroomNumber = classroomNumber;
        this.type = type;
        this.status = status;
    }

    public void edit(CampusType campusType, BuildingType buildingType, String floor, long classroomNumber, FacilityType type, FacilityStatus status) {
        this.campusType = campusType;
        this.buildingType = buildingType;
        this.floor = floor;
        this.classroomNumber = classroomNumber;
        this.type = type;
        this.status = status;
    }

}
