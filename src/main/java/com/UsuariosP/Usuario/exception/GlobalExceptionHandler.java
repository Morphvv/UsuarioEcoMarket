package com.UsuariosP.Usuario.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //400; Errores de validaciones
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejarErroresValidacion(MethodArgumentNotValidException ex){
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        log.warn("Error de validacion: {}", errores);
        return errores;
    }

    //404; recurso no encontrado
    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return error;
    }

    //409; valor unico duplicado
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> manejarDuplicado(DataIntegrityViolationException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Ya existe un registro con ese valor");
        log.warn("Violacion de integridad: {}", ex.getMessage());
        return error;
    }

    //500; error interno
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> manejarErrorGeneral(Exception ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        log.error("Error inesperado: {}", ex.getMessage());
        return error;
    }

}
