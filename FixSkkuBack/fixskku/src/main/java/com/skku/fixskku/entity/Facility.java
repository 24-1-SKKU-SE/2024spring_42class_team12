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

    @Column(name = "campus")  // 변경이 필요 없으면 생략 가능
    private String campus;

    @Column(name = "building")  // 변경이 필요 없으면 생략 가능
    private Long building;

    @Column(name = "floor")  // 변경이 필요 없으면 생략 가능
    private String floor;

    @Column(name = "classroom")  // 변경이 필요 없으면 생략 가능
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
