package com.skku.fixskku.common.domain;

import lombok.Getter;

@Getter
public enum ReportStatus {
    REPORT_RECEIVED("신고접수"),   // 신고접수
    REPAIR_REQUESTED("수리접수"),  // 수리접수
    UNDER_REPAIR("수리중"),      // 수리중
    REPAIR_COMPLETED("수리완료"),  // 수리 완료
    REJECTED("반려");           // 반려

    private final String name;

    ReportStatus(String name) {
        this.name = name;
    }

}
