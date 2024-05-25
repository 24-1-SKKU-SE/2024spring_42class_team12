package com.skku.fixskku.report.dto.req;

import com.skku.fixskku.common.domain.BuildingType;
import com.skku.fixskku.common.domain.CampusType;
import com.skku.fixskku.common.domain.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportReqDto {
    String token;
    CampusType campus;
    BuildingType building;
    String floor;
    long classroom;
    FacilityType facilityType;
    long facilityId;
    String description;
    String photoUrl;
}
