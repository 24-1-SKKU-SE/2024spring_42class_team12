package com.skku.fixskku.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")  // 데이터베이스 컬럼명과 일치하도록
    private Long facilityId;

    @Column(name = "campus")
    private String campus;

    @Column(name = "building")
    private Long building;

    @Column(name = "floor")
    private String floor;

    @Column(name = "classroom")
    private Long classroom;

    @Column(name = "facility_type")  // 데이터베이스 컬럼명과 일치하도록
    private String facilityType;

    @Column(name = "facility_status")  // 데이터베이스 컬럼명과 일치하도록
    private String facilityStatus;

    public Facility(String campus, Long building, String floor, Long classroom, String facilityType, String facilityStatus) {
        this.campus = campus;
        this.building = building;
        this.floor = floor;
        this.classroom = classroom;
        this.facilityType = facilityType;
        this.facilityStatus = facilityStatus;
    }

    public void edit(String campus, Long building, String floor, Long classroom, String facilityType, String facilityStatus) {
        this.campus = campus;
        this.building = building;
        this.floor = floor;
        this.classroom = classroom;
        this.facilityType = facilityType;
        this.facilityStatus = facilityStatus;
    }
}
