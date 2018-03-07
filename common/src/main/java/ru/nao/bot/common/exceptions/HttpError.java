package ru.nao.bot.common.exceptions;

import lombok.Getter;

@Getter
public class HttpError extends NgError {

    private final String url;
    private final String uri;

    public HttpError(String url, String uri, String message) {
        this(url, uri, message, null);
    }

    public HttpError(String url, String uri, String message, Throwable cause) {
        super(message, cause);
        this.url = url;
        this.uri = uri;
    }
}
