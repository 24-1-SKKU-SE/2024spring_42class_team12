package com.skku.fixskku.common.domain;

import lombok.Getter;

@Getter
public enum BuildingType {
    /*
     * 인문사회 캠퍼스
     */
    // 경영관
    BUS("경영관" , 33, 33),
    // 다산경제관
    DASAN("다산경제관", 32, 32),
    // 법학관
    LAW("법학관", 20, 20),
    // 수선관
    SUSUN("수선관", 61, 61),
    // 퇴계인문관
    TOEGYE("퇴계인문관", 31, 31),
    // 호암관
    HOAM("호암관", 50, 51),

    /*
     * 자연과학 캠퍼스
     */
    // 수성관
    SUSUNG("수성관", 05, 05),
    // 제1공학관
    ENG1("제1공학관", 22, 23),
    // 제2공학관
    ENG2("제2공학관", 26, 27),
    // 자연과학관
    SCI("자연과학관", 31, 32),
    // 기초학문관
    BASIC("기초학문관", 51, 51),
    // 생명공학관
    BIO("생명공학관", 62, 62),
    // 의학관
    MED("의학관", 71, 71),
    // 약학관
    PHARM("약학관", 53, 53),
    // 화학관
    CHEM("화학관", 33, 33),
    // 반도체관
    SEMI("반도체관", 40, 40);

    // 건물 이름
    private final String name;
    // 건물 번호(2개인 경우도 있어서 2개일 경우에는 각각 code1, code2에 담기고, 1개일 경우에는 code1과 code2에 같은 값이 담김
    private final int code1;
    private final int code2;

    BuildingType(String name, int code1, int code2) {
        this.name = name;
        this.code1 = code1;
        this.code2 = code2;
    }

    // 강의실 코드가 두개인지 확인하는 메서드
    public boolean hasTwoCodes(){
        return this.code1 != this.code2;
    }
}
