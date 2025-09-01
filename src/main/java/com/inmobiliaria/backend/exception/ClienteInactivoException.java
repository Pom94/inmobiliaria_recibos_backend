package com.inmobiliaria.backend.exception;

public class ClienteInactivoException extends Exception{
    public ClienteInactivoException(String mensaje){
        super(mensaje);
    }

}
