package com.inmobiliaria.backend.exception;

public class ContraseniaIncorrectaException extends Exception{
    public ContraseniaIncorrectaException(String mensaje){
        super(mensaje);
    }
}
