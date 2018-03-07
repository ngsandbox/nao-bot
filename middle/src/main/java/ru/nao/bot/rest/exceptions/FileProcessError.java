package ru.nao.bot.rest.exceptions;

public class FileProcessError extends NaoException {
    public FileProcessError(String message) {
        super(message);
    }

    public FileProcessError(String message, Exception ex) {
        super(message, ex);
    }
}
