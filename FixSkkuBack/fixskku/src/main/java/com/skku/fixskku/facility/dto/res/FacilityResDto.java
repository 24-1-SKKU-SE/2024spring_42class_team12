package com.skku.fixskku.facility.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacilityResDto {
    private long facilityId;
    private String facilityType;
    private String facilityStatus;
}
