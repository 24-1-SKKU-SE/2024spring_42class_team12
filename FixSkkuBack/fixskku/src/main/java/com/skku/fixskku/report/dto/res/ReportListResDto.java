package com.skku.fixskku.report.dto.res;

import com.skku.fixskku.report.domain.Report;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ReportListResDto {
    private long reportId;
    private long facilityId;
    private String token;
    private String description;
    private String photoUrl;
    private String reportStatus;
    private String creationDate;
    private String rejectionReason;
    private String campus;
    private String building;
    private String floor;
    private long classroom;
    private String facilityType;
    private String facilityStatus;

    public ReportListResDto(Report report){
        this.reportId = report.getId();
        this.facilityId = report.getFacility().getId();
        this.token = report.getTokenId();
        this.description = report.getDescription();
        this.photoUrl = report.getPhotoUrl();
        this.reportStatus = report.getStatus().getName();
        this.creationDate = formatDateTime(report.getCreationDate());
        this.rejectionReason = report.getRejectionReason();
        this.campus = report.getFacility().getCampusType().getCampusName();
        this.building = report.getFacility().getBuildingType().getName();
        this.floor = report.getFacility().getFloor();
        this.classroom = report.getFacility().getClassroomNumber();
        this.facilityType = report.getFacility().getType().getName();
        this.facilityStatus = report.getFacility().getStatus().getName();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
