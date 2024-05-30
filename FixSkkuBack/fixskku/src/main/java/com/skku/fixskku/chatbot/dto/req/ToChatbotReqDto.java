package com.skku.fixskku.chatbot.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ToChatbotReqDto {

    @JsonProperty("text")
    private String text;

    @JsonProperty("FAQ_id")
    private String faqId;

}
