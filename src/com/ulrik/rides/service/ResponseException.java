package com.ulrik.rides.service;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 31/03/2015
 * Time: 16:28
 */
public class ResponseException extends Exception {

    public ResponseException() {
    }

    public ResponseException(String detailMessage) {
        super(detailMessage);
    }

    public ResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResponseException(Throwable throwable) {
        super(throwable);
    }
}
