package com.UsuariosP.Usuario.service;

import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.repository.SesionRepository;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SesionServiceTest {

    @Mock
    private SesionRepository sesionRepository;

    @InjectMocks
    private SesionService sesionService;

    //Sesion de prueba
    private Sesion nuevaSesion(Long idSesion, Long idUsuario, String tokenSesion, LocalDateTime fechaInicio, LocalDateTime fechaExpiracion, String estadoSesion){
        Sesion s  = new Sesion();
        s.setIdSesion(idSesion);
        s.setIdUsuario(12345678L);  
        s.setTokenSesion("token-abc-123");
        s.setFechaInicio(fechaInicio);
        s.setFechaExpiracion(fechaExpiracion);
        s.setEstadoSesion(estadoSesion);
        return s;
    }

    @Test
    void iniciarSesion_FechaInicio_Estado(){
        //Given
        Sesion entrada = nuevaSesion(null, null, LocalDateTime.now().plusHours(2));
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(invocacion -> invocacion.getArgument(0));   

        //When
        Sesion resultado = sesionService.iniciarSesion(entrada);

        //Then
        assertNotNull(resultado);
        assertEquals("ACTIVA", resultado.getEstadoSesion());
        assertNotNull(resultado.getFechaInicio());
        verify(sesionRepository, times(1)).save(entrada);
    }

    @Test
    void listar_Sesiones(){
        //Given
        Sesion s1 = nuevaSesion(1L, "ACTIVA", LocalDateTime.now().plusHours(1));
        Sesion s2 = nuevaSesion(2L, "INACTIVA", LocalDateTime.now().minusHours(1));
        when(sesionRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        //When
        List<Sesion> resultado = sesionService.listarS();

        //Then
        assertEquals(2, resultado.size());
        verify(sesionRepository, times(1)).findAll();
    }

    @Test
    void modificarSesion_AcutalizarCampos(){ //Cuando existe la sesion
        //Given
        Sesion existente = nuevaSesion(1L, "ACTIVA", LocalDateTime.now().plusHours(1));
        Sesion datosNuevos = nuevaSesion(1L, "INACTIVA", LocalDateTime.now().plusHours(3));
        datosNuevos.setTokenSesion("token-nuevo-999");

        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sesionRepository.save(existente)).thenReturn(existente);

        //When
        Sesion resultado = sesionService.modificarSesion(1L, datosNuevos);

        //Then
        assertNotNull(resultado);
        assertEquals("token-nuevo-999", resultado.getTokenSesion());
        assertEquals("INACTIVA", resultado.getEstadoSesion());
        verify(sesionRepository, times(1)).save(existente);
    }

    @Test
    void validarSesion_RetornarSesion(){ 
        //Given
        Sesion existente = nuevaSesion(1L, "ACTIVA", LocalDateTime.now().plusHours(1));
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));

        //When
        Sesion resultado = sesionService.validarSesion(1L);

        //Then
        assertNotNull(resultado);
        assertEquals("ACTIVA", resultado.getEstadoSesion());
        verify(sesionRepository, never())save(any(Sesion.class));
    }

    @Test
    void validarSesion_expirada_null(){
        //Given
        Sesion existente = nuevaSesion(1L, "ACTIVA", LocalDateTime.now().minusHours(1));
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));

        //When
        Sesion resultado = sesionService.validarSesion(1L);

        //Then
        assertNull(resultado);
    }

    @Test
    void validarSesion_inactiva_null(){
        //Given
        Sesion existente = nuevaSesion(1L, "INACTIVA", LocalDateTime.now().plusHours(1));
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));

        //When 
        Sesion resultado = sesionService.validarSesion(1L);

        //Then
        assertNull(resultado);
    }
    
    @Test
    void validarSesion_null(){ //Cuando no existe la sesion
        //Given
        when(sesionRepository.findById(99L)).thenReturn(optional.empty());

        //When
        Sesion resultado = sesionService.validarSesion(99L);

        //Then
        assertNull(resultado);
    }

    @Test 
    void cerrarSesion_EstadoInactiva(){
        //Given
        Sesion existente = nuevaSesion(1L, "ACTIVA", LocalDateTime.now().plusHours(1));
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sesionRepository.save(existente)).thenReturn(existente);

        //When
        Sesion resultado = sesionService.cerrarSesion(1L);

        //Then
        assertNotNull(resultado);
        assertEquals("INACTIVA", resultado.getEstadoSesion());
        verify(sesionRepository, times(1)).save(existente);
    }

    @Test
    void cerrarSesion_null(){ //Cuando no existe la sesion y no se guarda
        //Given
        when(sesionRepository.findById(99L)).thenReturn(Optional.empty());

        //when
        Sesion resultado = sesionService.cerrarSesion(99L);

        //Then 
        assertNull(resultado);
        verify(sesionRepository, never()).save(any(Sesion.class));
    }

    @Test
    void eliminar(){
        //When
        sesionService.eliminar(1L);

        //Then
        verify(sesionRepository, times(1)).deleteById(1L);
    }
}
