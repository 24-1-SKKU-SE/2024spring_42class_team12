package com.skku.fixskku.facility.domain;

import com.skku.fixskku.common.domain.FacilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "classroom")
    private long classroomNumber;

    @Column(name = "building")
    private String building;

    @Column(name = "campus")
    private String campus;

    @Column(name = "facility_status")
    @Enumerated(EnumType.STRING)
    private FacilityStatus status;

    @Column(name = "facility_type")
    private String type;

    @Column(name = "floor")
    private String floor;
    public CampusType getCampusType() {
        return new CampusType(campus);
    }

    public BuildingType getBuildingType() {
        return new BuildingType(building);
    }

    public Type getType() {
        return new Type(type);
    }

    public static class CampusType {
        private final String campusName;

        public CampusType(String campusName) {
            this.campusName = campusName;
        }

        public String getCampusName() {
            return campusName;
        }
    }

    public static class BuildingType {
        private final String name;

        public BuildingType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Type {
        private final String name;

        public Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
