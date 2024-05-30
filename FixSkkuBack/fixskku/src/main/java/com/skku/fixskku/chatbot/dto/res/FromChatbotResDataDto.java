package com.skku.fixskku.chatbot.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FromChatbotResDataDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String campus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String building;

    private String classroom;

}

