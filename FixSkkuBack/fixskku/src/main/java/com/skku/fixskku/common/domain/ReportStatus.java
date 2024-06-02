package com.skku.fixskku.common.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum ReportStatus {
    REPORT_RECEIVED("신고 접수"),   // 신고 접수
    REPAIR_REQUESTED("수리 접수"),  // 수리 접수
    UNDER_REPAIR("수리 중"),      // 수리 중
    REPAIR_COMPLETED("수리 완료"),  // 수리 완료
    REJECTED("반려");           // 반려

    private final String name;

    ReportStatus(String name) {
        this.name = name;
    }

    public static List<String> getStatusNames() {
        return Arrays.stream(ReportStatus.values())
                .map(ReportStatus::getName)
                .collect(Collectors.toList());
    }

    public static ReportStatus fromName(String name) {
        return Arrays.stream(ReportStatus.values())
                .filter(status -> status.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status name: " + name));
    }
}
