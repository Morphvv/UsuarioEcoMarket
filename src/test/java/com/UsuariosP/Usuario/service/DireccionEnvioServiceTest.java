package com.UsuariosP.Usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.repository.DireccionEnvioRepository;

@ExtendWith(MockitoExtension.class)
class DireccionEnvioServiceTest {

    @Mock
    private DireccionEnvioRepository direccionEnvioRepository;

    @InjectMocks
    private DireccionEnvioService direccionEnvioService;

    //Prueba de dirrecion
    private DireccionEnvio direccionEnvio(Long idDireccion, Long idUsuario, String calle, String numero, String ciudad, String region, String codigoPostal, String referencia, Boolean direccionPrincipal, Boolean activa){
        DireccionEnvio d = new DireccionEnvio();
        d.setIdDireccion(idDireccion);
        if (idUsuario != null) {
            Usuario usuario = new Usuario();
            usuario.setRut(idUsuario);
            d.setUsuario(usuario);
        }
        d.setCalle(calle);
        d.setNumero(numero);
        d.setCiudad(ciudad);
        d.setRegion(region);
        d.setCodigoPostal(codigoPostal);
        d.setReferencia(referencia);
        d.setDireccionPrincipal(direccionPrincipal);
        d.setActiva(activa);
        return d;
    }

    @Test 
    void crear_AsignarDireccionPrincipal_Activa(){
        //Given
        DireccionEnvio entrada = direccionEnvio(null, 1L, "Calle Falsa", "123", "Santiago", "RM", "12345", "Cerca del parque", true, true);
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        DireccionEnvio resultado = direccionEnvioService.crear(entrada);

        //Then
        assertNotNull(resultado.getDireccionPrincipal());
        assertNotNull(resultado.getActiva());
    }

    @Test
    void crear_SinDireccionPrincipal_Activa(){
        //Given
        DireccionEnvio entrada = direccionEnvio(null, 1L, "Calle Falsa", "123", "Santiago", "RM", "12345", "Cerca del parque", false, false);
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        DireccionEnvio resultado = direccionEnvioService.crear(entrada);

        //Then
        assertNotNull(resultado.getDireccionPrincipal());
        assertNotNull(resultado.getActiva());
    }

    @Test
    void listar_RetornarDirecciones(){
        //Given
        DireccionEnvio d1 = direccionEnvio(1L, 1L, "Calle Falsa", "123", "Santiago", "RM", "12345", "Cerca del parque", true, true);
        DireccionEnvio d2 = direccionEnvio(2L, 1L, "Avenida Siempre Viva", "456", "Santiago", "RM", "67890", "Frente al supermercado", false, true);
        when(direccionEnvioRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        //When
        List<DireccionEnvio> resultado = direccionEnvioService.listar();

        //Then
        assertEquals(2, resultado.size());
        verify(direccionEnvioRepository, times(1)).findAll(); 
    }

    @Test
    void listarPorUsuario_DireccionesActiva(){
        //Given
        DireccionEnvio d1 = direccionEnvio(1L, 1L, "Calle Falsa", "123", "Santiago", "RM", "12345", "Cerca del parque", true, true);
        DireccionEnvio d2 = direccionEnvio(2L, 1L, "Avenida Siempre Viva", "456", "Santiago", "RM", "67890", "Frente al supermercado", false, true);
        when(direccionEnvioRepository.findByUsuarioRutAndActivaTrue(1L)).thenReturn(Arrays.asList(d1, d2));

        //When
        List<DireccionEnvio> resultado = direccionEnvioService.listarPorUsuario(1L);    

        //Then
        assertEquals(2, resultado.size());
        verify(direccionEnvioRepository, times(1)).findByUsuarioRutAndActivaTrue(1L);
    }

    @Test
    void modificarDireccion_ActualizarCampos(){ //Cunaod existe una direccion
        //Given
        DireccionEnvio direccionExistente = direccionEnvio(1L, 1L, "Calle Falsa", "123", "Santiago", "RM", "12345", "Cerca del parque", true, true);
        DireccionEnvio direccionActualizada = direccionEnvio(1L, 1L, "Avenida Siempre Viva", "456", "Santiago", "RM", "67890", "Frente al supermercado", false, true);

        when(direccionEnvioRepository.findById(1L)).thenReturn(Optional.of(direccionExistente));
        when(direccionEnvioRepository.save(direccionExistente)).thenReturn(direccionExistente);

        //When
        DireccionEnvio resultado = direccionEnvioService.modificarDireccionEnvio(1L, direccionActualizada);

        //Then
        assertNotNull(resultado);
        assertEquals("Avenida Siempre Viva", resultado.getCalle());
        assertEquals("456", resultado.getNumero());
        assertEquals("Santiago", resultado.getCiudad());
        assertEquals("RM", resultado.getRegion());
        assertEquals("67890", resultado.getCodigoPostal());
        assertEquals("Frente al supermercado", resultado.getReferencia());
        verify(direccionEnvioRepository, times(1)).save(direccionExistente);
    }

    @Test
    void marcarPrincipal(){ //Solo una direccion
        //Given
        Usuario propietario = new Usuario();
        propietario.setRut(12344567L);

        DireccionEnvio existente = direccionEnvio(1L, 12344567L, "Calle uwu", "123", "Santiago", "RM", "12345", "Cerca del parque", true, true);
        existente.setUsuario(propietario);
        DireccionEnvio otro = direccionEnvio(2L, 12344567L, "Avenida siempre viva", "456", "Santiago", "RM", "67890", "Frente al supermercado", false, true);
        otro.setUsuario(propietario);

        when(direccionEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(direccionEnvioRepository.findByUsuarioRut(12344567L)).thenReturn(Arrays.asList(existente, otro));
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        //When
        DireccionEnvio resultado = direccionEnvioService.marcarComoPrincipal(1L);

        //Then, una vez que se elige la otra se desactiva
        assertTrue(resultado.getDireccionPrincipal());
        assertFalse(otro.getDireccionPrincipal());
        verify(direccionEnvioRepository, times(1)).findByUsuarioRut(12344567L);
        verify(direccionEnvioRepository, times(1)).save(otro);
        verify(direccionEnvioRepository, times(1)).save(existente);
    }

    @Test
    void marcarPrincipal_Null(){ //Cuando no existe la direccion
        //Given
        when(direccionEnvioRepository.findById(99L)).thenReturn(Optional.empty());

        //When
        DireccionEnvio resultado = direccionEnvioService.marcarComoPrincipal(99L);

        //Then
        assertNull(resultado);
        verify(direccionEnvioRepository, never()).findByUsuarioRut(anyLong());
        verify(direccionEnvioRepository, never()).save(any(DireccionEnvio.class));
    }

    @Test
    void desactivarDireccion(){
        //Given 
        DireccionEnvio existente = direccionEnvio(1L, 12344567L, "Calle uwu", "123", "Santiago", "RM", "12345", "Cerca del parque", true, true);

        when(direccionEnvioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(direccionEnvioRepository.save(existente)).thenReturn(existente);

        //When
        DireccionEnvio resultado = direccionEnvioService.desactivar(1L);

        //Then
        assertNotNull(resultado);
        assertFalse(resultado.getActiva());
        verify(direccionEnvioRepository, times(1)).save(existente);
    }

    @Test
    void desactivarDireccion_null(){ //Cuando no existe la direccion y tampoco se guarda
        //Given
        when(direccionEnvioRepository.findById(99L)).thenReturn(Optional.empty());

        //When
        DireccionEnvio resultado = direccionEnvioService.desactivar(99L);

        //Then 
        assertNull(resultado);
        verify(direccionEnvioRepository, never()).save(any(DireccionEnvio.class));
    }

    @Test
    void eliminarDireccion(){
        //When
        direccionEnvioService.eliminar(1L);

        //Then
        verify(direccionEnvioRepository, times(1)).deleteById(1L);
    }

    
}
