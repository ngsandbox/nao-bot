package ru.nao.bot.common.face;

import lombok.Data;

@Data
public class BaseResponse {
    private String login;

    public BaseResponse(String login) {
        this.login = login;
    }
}
