package com.skku.fixskku.common.domain;

import lombok.Getter;

@Getter
public enum FacilityType {
    DESK("책상"),
    CHAIR("의자"),
    AIR_CONDITIONER("에어컨"),
    POWER_OUTLET("콘센트");

    private final String name;

    FacilityType(String name) {
        this.name = name;
    }

}
