package com.UsuariosP.Usuario.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.repository.CuentaUsuarioRepository;

@ExtendWith(MockitoExtension.class)
class CuentaUsuarioServiceTest {

    @Mock
    private CuentaUsuarioRepository cuentaUsuarioRepository;

    @InjectMocks
    private CuentaUsuarioService cuentaUsuarioService;

    //Armar la cuenta 
    private CuentaUsuario nuevaCuenta(Long idUsuario, String nombreUsuario, String email, String password, String estadoCuenta, LocalDateTime ultimoAcceso){
        CuentaUsuario c = new CuentaUsuario();
        c.setIdUsuario(idUsuario);
        c.setNombreUsuario(nombreUsuario);
        c.setEmail(email);
        c.setPassword(password);
        c.setRol("Cliente");
        c.setEstadoCuenta(estadoCuenta);
        c.setUltimoAcceso(null);
        return c;
    }

    @Test 
    void crear_AsignarRol_Estado_UltimoAcceso(){
        //Given
        CuentaUsuario entrada = nuevaCuenta(null, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        when(cuentaUsuarioRepository.save(any(CuentaUsuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        CuentaUsuario resultado = cuentaUsuarioService.crear(entrada);

        //Then 
        assertNotNull(resultado);
        assertEquals("CLIENTE", resultado.getRol());
        assertEquals("ACTIVA", resultado.getEstadoCuenta());
        assertNotNull(resultado.getUltimoAcceso());
        verify(cuentaUsuarioRepository, times(1)).save(any(CuentaUsuario.class));
    }

    @Test 
    void listar_RetornarCuentas(){
        //Given 
        CuentaUsuario c1 = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        CuentaUsuario c2 = nuevaCuenta(2L, "juanperez", "juanperez@example.com", "password456", "Activa", null);
        when(cuentaUsuarioRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        //When
        List<CuentaUsuario> resultado = cuentaUsuarioService.listar();
        
        //Then 
        assertEquals(2, resultado.size());
        verify(cuentaUsuarioRepository, times(1)).findAll();
    }

    @Test
    void modificarCuenta_ActualizarCampos(){ //Cuando existe la cuenta
        //Given 
        CuentaUsuario cuentaExistente = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        CuentaUsuario cuentaActualizada = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "newpassword123", "Activa", null);

        when(cuentaUsuarioRepository.findById(1L)).thenReturn(Optional.of(cuentaExistente));
        when(cuentaUsuarioRepository.save(cuentaExistente)).thenReturn(cuentaExistente);

        //When
        CuentaUsuario resultado = cuentaUsuarioService.modificarCuentaU(1L, cuentaActualizada);

        //Then
        assertNotNull(resultado);
        assertEquals("pepetapiaaa", resultado.getNombreUsuario());
        assertEquals("pepetapiaaa@example.com", resultado.getEmail());
        verify(cuentaUsuarioRepository, times(1)).save(cuentaExistente);
    }

    @Test
    void modificarCuenta_NoExiste_RetornarNull(){ //Cuando no existe la cuenta
        //Given
        when(cuentaUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        //When
        CuentaUsuario resultado = cuentaUsuarioService.modificarCuentaU(99L, new CuentaUsuario());

        //Then
        assertNull(resultado);
        verify(cuentaUsuarioRepository, never()).save(any(CuentaUsuario.class));
    }

    @Test
    void autenticar_RetornarCuenta_UltimoAcceso(){ //La cuenta y password tienen que coincidir 
        //Given
        CuentaUsuario cuentaExistente = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        CuentaUsuario cuentaAutenticada = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);

        when(cuentaUsuarioRepository.findByEmail("pepetapiaaa@example.com")).thenReturn(Optional.of(cuentaExistente));
        when(cuentaUsuarioRepository.save(cuentaExistente)).thenReturn(cuentaExistente);

        //When 
        CuentaUsuario resultado = cuentaUsuarioService.autenticar(cuentaAutenticada);

        //Then 
        assertNotNull(resultado);
        assertNotNull(resultado.getUltimoAcceso());
        verify(cuentaUsuarioRepository, times(1)).save(cuentaExistente);
    }

    @Test
    void autenticar_password_RetornarNull(){ //La cuenta existe pero la password no coincide
        //Given 
        CuentaUsuario cuentaExistente = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        CuentaUsuario cuentaAutenticada = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "wrongpassword", "Activa", null);

        when(cuentaUsuarioRepository.findByEmail("pepetapiaaa@example.com")).thenReturn(Optional.of(cuentaExistente));

        //When 
        CuentaUsuario resultado = cuentaUsuarioService.autenticar(cuentaAutenticada);

        //Then 
        assertNull(resultado);
        verify(cuentaUsuarioRepository, times(0)).save(cuentaExistente);
    }

    @Test
    void autenticar_email_retornoarNull(){ //No hay ninguna cuenta con ese email
        //Given 
        CuentaUsuario cuentaAutenticada = nuevaCuenta(null, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        when(cuentaUsuarioRepository.findByEmail("pepetapiaaa@example.com")).thenReturn(Optional.empty());

        //When
        CuentaUsuario resultado = cuentaUsuarioService.autenticar(cuentaAutenticada);


        //Then
        assertNull(resultado);
        verify(cuentaUsuarioRepository, never()).save(any(CuentaUsuario.class));
    }

    @Test
    void desactivarCuenta_CambiarEstado(){ //Dejamos inactiva la cuenta
        //Given
        CuentaUsuario cuentaExistente = nuevaCuenta(1L, "pepetapiaaa", "pepetapiaaa@example.com", "password123", "Activa", null);
        when(cuentaUsuarioRepository.findById(1L)).thenReturn(Optional.of(cuentaExistente));
        when(cuentaUsuarioRepository.save(cuentaExistente)).thenReturn(cuentaExistente);

        //When
        CuentaUsuario resultado = cuentaUsuarioService.desactivar(1L);

        //Then
        assertNotNull(resultado);
        assertEquals("INACTIVA", resultado.getEstadoCuenta());
        verify(cuentaUsuarioRepository, times(1)).save(cuentaExistente);
    }

    @Test
    void desactivarCuenta_RetornarNull(){ //Cuando no existe la cuenta y no guardamos
        //Given
        when(cuentaUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        //When
        CuentaUsuario resultado = cuentaUsuarioService.desactivar(99L);

        //Then 
        assertNull(resultado);
        verify(cuentaUsuarioRepository, never()).save(any(CuentaUsuario.class));
    }

    @Test
    void eliminarCuenta(){
        //When
         cuentaUsuarioService.eliminar(1L);

         //Then 
        verify(cuentaUsuarioRepository, times(1)).deleteById(1L);
    }
    
}
