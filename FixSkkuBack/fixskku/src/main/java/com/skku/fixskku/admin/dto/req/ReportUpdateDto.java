package com.skku.fixskku.admin.dto.req;

import lombok.Data;

@Data
public class ReportUpdateDto {
    private Long reportId;
    private Long facilityId;
    private String reportStatus;
    private String rejectionReason;
}
