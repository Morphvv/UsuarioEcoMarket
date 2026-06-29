package com.UsuariosP.Usuario.service;

import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;
import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.repository.SesionRepository;

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

    private Sesion nuevaSesion(Long idSesion, Long idUsuario, String tokenSesion, LocalDateTime fechaInicio, LocalDateTime fechaExpiracion, String estadoSesion) {
        Sesion s = new Sesion();
        s.setIdSesion(idSesion);
        s.setIdUsuario(idUsuario);
        s.setTokenSesion(tokenSesion);
        s.setFechaInicio(fechaInicio);
        s.setFechaExpiracion(fechaExpiracion);
        s.setEstadoSesion(estadoSesion);
        return s;
    }

    @Test
    void iniciarSesion_FechaInicio_Estado() {
        Sesion entrada = nuevaSesion(null, 12345678L, "token-abv", LocalDateTime.now(), LocalDateTime.now().plusHours(2), null);
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sesion resultado = sesionService.iniciarSesion(entrada);

        assertNotNull(resultado);
        assertEquals("ACTIVA", resultado.getEstadoSesion());
        assertNotNull(resultado.getFechaInicio());
        verify(sesionRepository, times(1)).save(entrada);
    }

    @Test
    void listar_Sesiones() {
        Sesion s1 = nuevaSesion(1L, 12345678L, "token-1", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "ACTIVA");
        Sesion s2 = nuevaSesion(2L, 12345678L, "token-2", LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), "INACTIVA");
        when(sesionRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Sesion> resultado = sesionService.listarS();

        assertEquals(2, resultado.size());
        verify(sesionRepository, times(1)).findAll();
    }

    @Test
    void modificarSesion_AcutalizarCampos() {
        Sesion existente = nuevaSesion(1L, 12345678L, "token-abc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "ACTIVA");
        Sesion datosNuevos = nuevaSesion(1L, 12345678L, "token-nuevo-999", LocalDateTime.now(), LocalDateTime.now().plusHours(3), "INACTIVA");

        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sesionRepository.save(existente)).thenReturn(existente);

        Sesion resultado = sesionService.modificarSesion(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("token-nuevo-999", resultado.getTokenSesion());
        assertEquals("INACTIVA", resultado.getEstadoSesion());
        verify(sesionRepository, times(1)).save(existente);
    }

    @Test
    void modificarSesion_NoExiste_LanzaExcepcion() {
        //Given
        when(sesionRepository.findById(99L)).thenReturn(Optional.empty());

        //When - Then
        assertThrows(RecursoNoEncontradoException.class, () -> sesionService.modificarSesion(99L, new Sesion()));
    }

    @Test
    void validarSesion_RetornarSesion() {
        Sesion existente = nuevaSesion(1L, 12345678L, "token-abc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "ACTIVA");
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));

        Sesion resultado = sesionService.validarSesion(1L);

        assertNotNull(resultado);
        assertEquals("ACTIVA", resultado.getEstadoSesion());
        verify(sesionRepository, never()).save(any(Sesion.class));
    }

    @Test
    void validarSesion_expirada_null() {
        Sesion existente = nuevaSesion(1L, 12345678L, "token-abc", LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), "ACTIVA");
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));

        Sesion resultado = sesionService.validarSesion(1L);

        assertNull(resultado);
    }

    @Test
    void validarSesion_inactiva_null() {
        Sesion existente = nuevaSesion(1L, 12345678L, "token-abc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "INACTIVA");
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));

        Sesion resultado = sesionService.validarSesion(1L);

        assertNull(resultado);
    }

    @Test
    void validarSesion_null() {
        when(sesionRepository.findById(99L)).thenReturn(Optional.empty());

        Sesion resultado = sesionService.validarSesion(99L);

        assertNull(resultado);
    }

    @Test
    void cerrarSesion_EstadoInactiva() {
        Sesion existente = nuevaSesion(1L, 12345678L, "token-abc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "ACTIVA");
        when(sesionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(sesionRepository.save(existente)).thenReturn(existente);

        Sesion resultado = sesionService.cerrarSesion(1L);

        assertNotNull(resultado);
        assertEquals("INACTIVA", resultado.getEstadoSesion());
        verify(sesionRepository, times(1)).save(existente);
    }

    @Test
    void cerrarSesion_null() {
        when(sesionRepository.findById(99L)).thenReturn(Optional.empty());

        Sesion resultado = sesionService.cerrarSesion(99L);

        assertNull(resultado);
        verify(sesionRepository, never()).save(any(Sesion.class));
    }

    @Test
    void eliminar() {
        sesionService.eliminar(1L);
        verify(sesionRepository, times(1)).deleteById(1L);
    }
}
