package com.skku.fixskku.common.domain;

import lombok.Getter;

@Getter
public enum FacilityStatus {
    USABLE("정상"), UNUSABLE("고장");

    private final String name;

    FacilityStatus(String name) {
        this.name = name;
    }
}
