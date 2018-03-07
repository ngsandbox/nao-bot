package ru.nao.bot.common.speech.entities;

import lombok.Data;

@Data
public class BaseResponse {
    private String text;

    public BaseResponse(String text) {
        this.text = text;
    }
}
