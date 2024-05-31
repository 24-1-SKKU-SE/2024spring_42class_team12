package com.skku.fixskku.chatbot.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ToFrontResDto {

    @JsonProperty("response")
    private String response;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("campus")
    private String campus;

    @JsonProperty("building")
    private String building;

    @JsonProperty("classroom")
    private String classroom;
}
