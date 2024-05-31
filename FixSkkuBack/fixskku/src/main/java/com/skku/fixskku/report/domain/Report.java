package com.skku.fixskku.report.domain;

import com.skku.fixskku.common.domain.ReportStatus;
import com.skku.fixskku.facility.domain.Facility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reports")
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reportid")
    private long id;

    @ManyToOne
    @JoinColumn(name = "facilityid")
    private Facility facility;

    @Column(name = "token")
    private String tokenId;

    @Column(name = "description")
    private String description;

    @Column(name = "photourl")
    private String photoUrl;

    @Column(name = "reportstatus")
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(name = "creationdate")
    private LocalDateTime creationDate;

    @Column(name = "rejectionreason")
    private String rejectionReason;

}
