package ru.nao.bot.rest.exceptions;

public class NaoException extends RuntimeException {

    public NaoException(String message) {
        super(message);
    }

    public NaoException(String message, Exception ex) {
        super(message, ex);
    }
}
