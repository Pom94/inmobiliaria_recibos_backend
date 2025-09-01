package com.inmobiliaria.backend.controller.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.inmobiliaria.backend.exception.AdminNoEncontradoException;
import com.inmobiliaria.backend.exception.AdminYaExisteException;
import com.inmobiliaria.backend.exception.ClienteInactivoException;
import com.inmobiliaria.backend.exception.ClienteNoEncontradoException;
import com.inmobiliaria.backend.exception.ContraseniaIncorrectaException;
import com.inmobiliaria.backend.exception.GenerarPDFException;
import com.inmobiliaria.backend.exception.PropiedadInactivaException;
import com.inmobiliaria.backend.exception.PropiedadNoEncontradaException;
import com.inmobiliaria.backend.exception.ReciboNoEncontradoException;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = {AdminYaExisteException.class})
    protected ResponseEntity<Object> handlerExisteException(Exception ex, WebRequest request){
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {AdminNoEncontradoException.class, ReciboNoEncontradoException.class, ClienteNoEncontradoException.class, PropiedadNoEncontradaException.class})
    protected ResponseEntity<Object> handlerNoEncontrado(Exception ex, WebRequest request){
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ContraseniaIncorrectaException.class})
    protected ResponseEntity<Object> handlerIncorrecto(Exception ex, WebRequest request){
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {GenerarPDFException.class})
    protected ResponseEntity<Object> handlerGenerarPDFException(Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    @ExceptionHandler(value = {ClienteInactivoException.class, PropiedadInactivaException.class})
    protected ResponseEntity<Object> handlerInactivo(Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
