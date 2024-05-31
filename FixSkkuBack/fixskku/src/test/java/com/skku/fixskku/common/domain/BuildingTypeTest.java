package com.skku.fixskku.common.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class BuildingTypeTest {

    @Test
    void getBuildingNameWithCampusAndClassroom() {
        //given
        //when
        String buildingName1 = BuildingType.getBuildingNameWithCampusAndClassroom(
                CampusType.SEOUL_CAMPUS.getCampusName(), "33022"
        );
        String buildingName2 = BuildingType.getBuildingNameWithCampusAndClassroom(
                CampusType.SEOUL_CAMPUS.getCampusName(), "51022"
        );
        String buildingName3 = BuildingType.getBuildingNameWithCampusAndClassroom(
                CampusType.SUWON_CAMPUS.getCampusName(), "51022"
        );

        //then
        Assertions.assertEquals("경영관", buildingName1);
        Assertions.assertEquals("호암관", buildingName2);
        Assertions.assertEquals("기초학문관", buildingName3);
    }
}