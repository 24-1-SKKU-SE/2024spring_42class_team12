package com.skku.fixskku.common.domain;

import lombok.Getter;

@Getter
public enum CampusType {
    SEOUL_CAMPUS("인문사회과학캠퍼스", 1),
    SUWON_CAMPUS("자연과학캠퍼스", 2);

    private final String campusName;
    private final int number;

    CampusType(String campusName, int number) {
        this.campusName = campusName;
        this.number = number;
    }

    // 문자열 캠퍼스 이름으로 캠퍼스 타입 객체를 반환하는 메서드
    public static CampusType getCampusTypeFromString(String campus){
        if(campus.equals(SEOUL_CAMPUS.getCampusName())) return SEOUL_CAMPUS;
        if(campus.equals(SUWON_CAMPUS.getCampusName())) return SUWON_CAMPUS;
        else throw new IllegalArgumentException();
    }
}
