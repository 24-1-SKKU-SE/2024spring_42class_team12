package com.skku.fixskku.chatbot.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skku.fixskku.chatbot.constants.AnswerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ChatbotResDto {

    @JsonProperty("answerType")
    private AnswerType answerType;

    @JsonProperty("text")
    private String text;

    @JsonProperty("FAQ_id")
    private long faqId;

    @JsonProperty("data")
    private FromChatbotResDataDto data;

}
