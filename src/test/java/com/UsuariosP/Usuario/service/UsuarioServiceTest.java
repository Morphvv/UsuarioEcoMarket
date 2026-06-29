package com.UsuariosP.Usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.UsuariosP.Usuario.exception.RecursoNoEncontradoException;
import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

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
    void guardarUsuario_SinCuenta(){
        //Given
        Usuario u = nuevoUsuario(2L, "Ana", "Martínez", "ana@gmail.com", "987654321", null, "ACTIVO");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //When
        Usuario resultado = usuarioService.guardarUsuario(u);

        //Then
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(u);
    }

    @Test
    void guardarUsuario_ConCuenta(){
        //Given
        CuentaUsuario cuenta = new CuentaUsuario();
        Usuario u = nuevoUsuario(3L, "Pedro", "López", "pedro@gmail.com", "912345678", null, "ACTIVO");
        u.setCuentaUsuario(cuenta);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //When
        Usuario resultado = usuarioService.guardarUsuario(u);

        //Then
        assertNotNull(resultado);
        assertEquals(u, cuenta.getUsuario());
        verify(usuarioRepository, times(1)).save(u);
    }

    @Test
    void crear_AsignarFechaYEstado(){
        Usuario entrada = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, null);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.crear(entrada);

        assertNotNull(resultado.getFechaRegistro());
        assertEquals("ACTIVO", resultado.getEstadoUsuario());
        assertNotNull(resultado.getRut());
        assertEquals("Pepe", resultado.getNombre());
        assertEquals("Tapia", resultado.getApellido());
        assertEquals("pepetapiaaa@gmail.com", resultado.getEmail());
        assertEquals("5691234567", resultado.getTelefono());
        verify(usuarioRepository, times(1)).save(entrada);
    }

    @Test
    void listar_RetornarUsuarios(){
        Usuario u1 = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, "ACTIVO");
        Usuario u2 = nuevoUsuario(123456782L, "María", "González", "mariagonzalez@gmail.com", "5691234568", null, "ACTIVO");
        List<Usuario> usuariosEsperados = Arrays.asList(u1, u2);

        when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);

        List<Usuario> resultado = usuarioService.listar();

        assertEquals(usuariosEsperados, resultado);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_UsuarioExistente(){
        Long rut = 123456781L;
        Usuario usuarioEsperado = nuevoUsuario(rut, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, "ACTIVO");
        when(usuarioRepository.findById(rut)).thenReturn(Optional.of(usuarioEsperado));

        Usuario resultado = usuarioService.buscarPorId(rut);

        assertNotNull(resultado);
        assertEquals(usuarioEsperado, resultado);
        verify(usuarioRepository, times(1)).findById(rut);
    }

    @Test
    void buscarPorId_UsuarioNoExiste_LanzaExcepcion(){
        when(usuarioRepository.findById(99999999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.buscarPorId(99999999L));
        verify(usuarioRepository, times(1)).findById(99999999L);
    }

    @Test
    void modificarUsuario_ActualizarDatos(){
        Usuario existente = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, "ACTIVO");
        Usuario actualizado = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, "ACTIVO");

        when(usuarioRepository.findById(123456781L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario resultado = usuarioService.modificarUsuario(123456781L, actualizado);

        assertNotNull(resultado);
        assertEquals("Pepe", resultado.getNombre());
        assertEquals("Tapia", resultado.getApellido());
        assertEquals("pepetapiaaa@gmail.com", resultado.getEmail());
        assertEquals("5691234567", resultado.getTelefono());
        verify(usuarioRepository, times(1)).findById(123456781L);
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void modificarUsuario_NoExiste_LanzaExcepcion(){
        //Given
        when(usuarioRepository.findById(99999999L)).thenReturn(Optional.empty());

        //When - Then
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.modificarUsuario(99999999L, new Usuario()));
    }

    @Test
    void desactivarUsuario_CambiarEstado(){
        Usuario existente = nuevoUsuario(123456781L, "Pepe", "Tapia", "pepetapiaaa@gmail.com", "5691234567", null, "ACTIVO");
        when(usuarioRepository.findById(123456781L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(existente)).thenReturn(existente);

        Usuario resultado = usuarioService.desactivar(123456781L);

        assertNotNull(resultado);
        assertEquals("INACTIVO", resultado.getEstadoUsuario());
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void desactivar_NoExiste_LanzaExcepcion(){
        //Given
        when(usuarioRepository.findById(99999999L)).thenReturn(Optional.empty());

        //When - Then
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.desactivar(99999999L));
    }

    @Test
    void eliminarUsuario_rut(){
        usuarioService.eliminar(123456781L);
        verify(usuarioRepository, times(1)).deleteById(123456781L);
    }
}
