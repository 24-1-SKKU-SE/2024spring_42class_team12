package com.skku.fixskku.chatbot.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class FromChatbotResDataDto {
    private long classroom;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String broken_item;

}

