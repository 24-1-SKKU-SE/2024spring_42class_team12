package com.skku.fixskku.facility.controller;

import com.skku.fixskku.dto.res.FacilityResDto;
import com.skku.fixskku.entity.Facility;
import com.skku.fixskku.facility.service.FacilityService;
import com.skku.fixskku.apipayload.ResponseApi;
import com.skku.fixskku.apipayload.ResponseStatus;
import com.skku.fixskku.apipayload.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fac")
public class FacilityController {

    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping("/{classroom}")
    public ResponseEntity<?> getFacilitiesByStatus(@PathVariable Long classroom, @RequestHeader(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseApi.badRequest();
        }

        try {
            List<Facility> facilities = facilityService.findFacilitiesByStatus(classroom);
            if (facilities.isEmpty()) {
                throw new GeneralException(ResponseStatus._BAD_REQUEST);
            }

            List<FacilityResDto> facilityResDtos = facilities.stream()
                    .map(facility -> new FacilityResDto(facility.getFacilityId(), facility.getFacilityType(), facility.getFacilityStatus()))
                    .collect(Collectors.toList());

            return ResponseApi.of(ResponseStatus._FACILITY_INFO_SUCCESS, facilityResDtos);
        } catch (GeneralException e) {
            return ResponseApi.of(e.getStatus());
        } catch (Exception e) {
            return ResponseApi.serverError();
        }
    }
}
