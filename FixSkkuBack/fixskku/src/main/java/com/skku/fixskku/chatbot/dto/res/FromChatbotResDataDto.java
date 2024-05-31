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

    private String classroom;

}

