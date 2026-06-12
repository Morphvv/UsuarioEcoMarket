package com.UsuariosP.Usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    // Agregar tus pruebas unitarias para el servicio de usuario
    private Usuario nuevoUsuario(Long rut, String nombre, String apellido, String email, String telefono, LocalDateTime fechaRegistro, String estadoUsuario ){
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setEmail(email);
        u.setTelefono(telefono);
        u.setFechaRegistro(fechaRegistro);
        u.setEstadoUsuario(estadoUsuario);
        return u;
    }

    @Test 
    void crear_AsignarFechaYEstado(){
        //El usuario sin fecha ni estado
        Usuario entrada = nuevoUsuario(123456781, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocacion.getArgument(0));

        //When, crear el usuario :v
        Usuario resultado = usuarioService.crear(entrada);

        //Then. el service aplica regla de negocio
        assertNotNull(resultado.getFechaRegistro());
        assertEquals("Activo", resultado.getEstadoUsuario());
        assertNotNull(resultado.getRut());
        assertEquals("Pepe", resultado.getNombre());
        assertEquals("Tapia", resultado.getApellido());
        assertEquals("pepetapiaaa@gmail.com", resultado.getEmail());
        assertEquals("5691234567", resultado.getTelefono());
        verify(usuarioRepository, time(1)).save(entrada);
    }

    @Test
    void listar_RetornarUsuarios(){
        //Given
        Usuario u1 = nuevoUsuario(123456781, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);
        Usuario u2 = nuevoUsuario(123456782, "María", "González", "mariagonzalez@gmail.com", "5691234568", null, null);
        List<Usuario> usuariosEsperados = Arrays.asList(u1, u2);

        when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);

        //When
        List<Usuario> resultado = usuarioService.listar();

        //Then
        assertEquals(usuariosEsperados, resultado);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test 
    void buscarPorRut_RetornarUsuario(){ //Si es que existe el usuario obviamente
        //Given
        Long rut = 123456781L;
        Usuario usuarioEsperado = nuevoUsuario(rut, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);
        when(usuarioRepository.findById(rut)).thenReturn(Optional.of(usuarioEsperado));

        //When 
        Usuario resultado = usuarioService.buscarPorRut(rut);

        //Then 
        assertNotNull(resultado);
        assertEquals(usuarioEsperado, resultado);
        verify(usuarioRepository, times(1)).findById(rut);
    }
    
    @Test
    void buscarPorRut_RetornarNull(){ //Cuando no existe el usuario
        //Given
        when(usuarioRepository.findByRut(99999999L)).thenReturn(Optional.empty());

        //When 
        Usuario resultado = usuarioService.buscarPorRut(99999999L);

        //Then 
        assertNull(resultado);
        verify(usuarioRepository, times(1)).findByRut(99999999L);
    }

    @Test
    void modificarUsuario_ActualizarDatos(){ //Cuando el usuario existe
        //Usuario creado en labase fake
        Usuario existente = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);
        Usuario actualizado = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);

        when(usuarioRepository.findByRut(123456781L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);

        //When
        Usuario resultado = usuarioService.modificarUsuario(123456781L, actualizado);

        //Then, se acutalizan campos editables
        assertNotNull(resultado);
        assertEquals("Pepe", resultado.getNombre());
        assertEquals("Tapia", resultado.getApellido());
        assertEquals("pepetapiaaa@gmail.com", resultado.getEmail());
        assertEquals("5691234567", resultado.getTelefono());
        verify(usuarioRepository, times(1)).findByRut(123456781L);
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void desactivarUsuario_CambiarEstado(){ //Cuando existe, y dejamos inactivo el estado
        //Given
        Usuario existente = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);
        when(usuarioRepository.findByRut(123456781L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);

        //When :v
        Usuario resultado = usuarioService.desactivarUsuario(123456781L);

        //Then, se cambia el estado a inactivo
        assertNotNull(resultado);
        assertEquals("Inactivo", resultado.getEstadoUsuario());
        verify(usuarioRepository, times(1)).save(existente);
    }

    Test 
    void desactivarUsuario_RetornarNull(){ //Cuando no existe el usuario y no guardamos 
        //Given 
        when(usuarioRepository.findByRut(99999999L)).thenReturn(Optional.empty());

        //When
        Usuario resultado = usuarioService.desactivarUsuario(99999999L);

        //Then, cuando no se encuentra nada y no se guarda nada
        assertNull(resultado);
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    @Test
    void eliminarUsuario_rut(){
        //When
        usuarioService.eliminarUsuario(123456781L);

        //Then
        verify(usuarioRepository, times(1)).deleteById(123456781L);
    }

    
}
