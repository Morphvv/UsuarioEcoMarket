package com.UsuariosP.Usuario.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Test
    void manejarErroresValidacion(){
        //Given
        FieldError campoError = new FieldError("cuentaUsuario", "email", "no puede estar vacío");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(campoError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        //When
        Map<String, String> resultado = handler.manejarErroresValidacion(methodArgumentNotValidException);

        //Then
        assertNotNull(resultado);
        assertEquals("no puede estar vacío", resultado.get("email"));
    }

    @Test
    void manejarRecursoNoEncontrado(){
        //Given
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("no se encontró el recurso");

        //When
        Map<String, String> resultado = handler.manejarRecursoNoEncontrado(ex);

        //Then
        assertNotNull(resultado);
        assertEquals("no se encontró el recurso", resultado.get("error"));
    }
}
