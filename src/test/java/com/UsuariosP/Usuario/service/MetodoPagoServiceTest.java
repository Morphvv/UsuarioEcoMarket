package com.UsuariosP.Usuario.service;

import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.repository.MetodoPagoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetodoPagoServiceTest {

    @Mock
    private MetodoPagoRepository metodoPagoRepository; 

    @InjectMocks
    private MetodoPagoService metodoPagoService;

    //Metodo de pago para prueba
    private MetodoPago nuevoMetodoPago(Long idMetodoPago, Long idUsuario, String tipoPago, String proveedorPago, String tokenPago, String ultimosDigitos, String alias, Date fechaExpiracion, String titular, Boolean activo, Boolean principal){
        MetodoPago m = new MetodoPago();
        if (idMetodoPago != null) {
            m.setIdMetodoPago(idMetodoPago);
        }
        if (idUsuario != null) {
            Usuario usuario = new Usuario();
            usuario.setRut(idUsuario);
            m.setUsuario(usuario);
        }
        m.setTipoPago(tipoPago);
        m.setProveedorPago(proveedorPago);
        m.setTokenPago(tokenPago);
        m.setUltimosDigitos(ultimosDigitos);
        m.setAlias(alias);
        m.setFechaExpiracion(fechaExpiracion);
        m.setTitular(titular);
        m.setActivo(activo);
        m.setPrincipal(principal);
        return m;
    }

    @Test
    void crear_AsignarActivo_Principal(){
        //Given
        MetodoPago entrada = nuevoMetodoPago(null, 1L, "Tarjeta de Crédito", "Visa", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", null, null);
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        MetodoPago resultado = metodoPagoService.crear(entrada);

        //Then
        assertNotNull(resultado);
        assertTrue(resultado.getActivo());
        assertFalse(resultado.getPrincipal());
        verify(metodoPagoRepository, times(1)).save(entrada);
    }

    @Test
    void crear_AsignarPrincipal(){
        //Given 
        MetodoPago entrada = nuevoMetodoPago(null, 1L, "Tarjeta de Crédito", "Visa", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", true, null);
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        MetodoPago resultado = metodoPagoService.crear(entrada);

        //Then
        assertTrue(resultado.getActivo());
        assertTrue(resultado.getPrincipal());
    }
    
    @Test
    void listar_RetornarMetodosPago(){
        //Given
        MetodoPago m1 = nuevoMetodoPago(1L, 1L, "Tarjeta de Crédito", "Visa", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", true, true);
        MetodoPago m2 = nuevoMetodoPago(2L, 1L, "Tarjeta de Débito", "MasterCard", "token456", "5678", "Mi MasterCard", Date.valueOf("2024-12-31"), "Juan Perez", true, false);
        when(metodoPagoRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        //When
        List<MetodoPago> resultado = metodoPagoService.listar();

        //Then
        assertEquals(2, resultado.size());
        verify(metodoPagoRepository, times(1)).findAll();
    }

    @Test 
    void listarPorUsuario_RetornarMetodosActivo(){
        //Given
        MetodoPago m1 = nuevoMetodoPago(1L, 1L, "Tarjeta de Crédito", "Visa", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", true, true);
        when(metodoPagoRepository.findByUsuarioRutAndActivoTrue(1L)).thenReturn(Arrays.asList(m1));

        //When
        List<MetodoPago> resultado = metodoPagoService.listarPorUsuario(1L);

        //Then
        assertEquals(1, resultado.size());
        verify(metodoPagoRepository, times(1)).findByUsuarioRutAndActivoTrue(1L);
    }

    @Test
    void modificarMetodoPago_ActualizarCampos(){ //Cuando existe el metodo
        //Given
        MetodoPago metodoExistente = nuevoMetodoPago(1L, 1L, "CREDITO", "Transbank", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", true, false);
        MetodoPago metodoActualizado = nuevoMetodoPago(null, null, "DEBITO", null, null, null, null, null, null, null, null);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(metodoExistente));
        when(metodoPagoRepository.save(metodoExistente)).thenReturn(metodoExistente);

        //When
        MetodoPago resultado = metodoPagoService.modificarMetodoPago(1L, metodoActualizado);

        //Then
        assertNotNull(resultado);
        assertEquals("DEBITO", resultado.getTipoPago());
        verify(metodoPagoRepository, times(1)).save(metodoExistente);
    }

    @Test
    void marcarPrincipal_metodo(){
        //Given
        Usuario propietario = new Usuario();
        propietario.setRut(12345678L);

        MetodoPago existente = nuevoMetodoPago(1L, 12345678L, "CREDITO", "Transbank", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", true, false);
        existente.setUsuario(propietario);
        MetodoPago otro = nuevoMetodoPago(2L, 12345678L, "DEBITO", "Transbank", "token456", "5678", "Mi MasterCard", Date.valueOf("2025-12-31"), "Juan Perez", true, true);
        otro.setUsuario(propietario);

        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.findByUsuarioRut(12345678L)).thenReturn(Arrays.asList(otro));
        when(metodoPagoRepository.save(any(MetodoPago.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        MetodoPago resultado = metodoPagoService.marcarComoPrincipal(1L);

        //Then
        assertTrue(resultado.getPrincipal());
        assertFalse(otro.getPrincipal());
        verify(metodoPagoRepository, times(1)).findByUsuarioRut(12345678L);
        verify(metodoPagoRepository, times(1)).save(otro);
        verify(metodoPagoRepository, times(1)).save(existente);
    }

    @Test
    void marcarPrincipal_Null(){ 
        //Given
        when(metodoPagoRepository.findById(99L)).thenReturn(Optional.empty());

        //When
        MetodoPago resultado = metodoPagoService.marcarComoPrincipal(99L);

        //Then 
        assertNull(resultado);
        verify(metodoPagoRepository, never()).findByUsuarioRut(anyLong());
        verify(metodoPagoRepository, never()).save(any(MetodoPago.class));
    }

    @Test
    void desactivar_DejarActivo(){ //Cuando existe un metodo
        //Given
        MetodoPago existente = nuevoMetodoPago(1L, 1L, "CREDITO", "Transbank", "token123", "1234", "Mi Visa", Date.valueOf("2025-12-31"), "Juan Perez", true, true);
        when(metodoPagoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(metodoPagoRepository.save(existente)).thenReturn(existente);

        //When
        MetodoPago resultado = metodoPagoService.desactivar(1L);

        //Then
        assertNotNull(resultado);
        assertFalse(resultado.getActivo());
        verify(metodoPagoRepository, times(1)).save(existente);
    }

    @Test
    void desactivar_Null(){ //Cuando no existe el metodo y no se guarda 
        //Given
        when(metodoPagoRepository.findById(99L)).thenReturn(Optional.empty());

        //When
        MetodoPago resultado = metodoPagoService.desactivar(99L);

        //Then
        assertNull(resultado);
        verify(metodoPagoRepository, never()).save(any(MetodoPago.class));
    }

    @Test
    void eliminar(){
        //When
        metodoPagoService.eliminar(1L);
        
        //Then
        verify(metodoPagoRepository, times(1)).deleteById(1L);
    }
}
