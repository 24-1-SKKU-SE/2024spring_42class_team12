package com.skku.fixskku.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacilityResDto {
    private Long facilityId;
    private String facilityType;
    private String facilityStatus;
}
