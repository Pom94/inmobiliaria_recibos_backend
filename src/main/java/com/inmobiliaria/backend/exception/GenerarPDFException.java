package com.inmobiliaria.backend.exception;

public class GenerarPDFException extends RuntimeException {
    public GenerarPDFException(String message) {
        super(message);
    }

    public GenerarPDFException(String message, Throwable cause) {
        super(message, cause);
    }
}