package ru.nao.bot.common.nao.exceptions;

import ru.nao.bot.common.exceptions.NgError;

public class RequestNotFound extends NgError {
    public RequestNotFound(String message) {
        super(message);
    }

    public RequestNotFound(String message, Exception ex) {
        super(message, ex);
    }
}
