package com.skku.fixskku.common.domain;

import lombok.Getter;

@Getter
public enum CampusType {
    SEOUL_CAMPUS("인문사회캠퍼스", 1),
    SUWON_CAMPUS("자연과학캠퍼스", 2);

    private final String campusName;
    private final int number;

    CampusType(String campusName, int number) {
        this.campusName = campusName;
        this.number = number;
    }
}
