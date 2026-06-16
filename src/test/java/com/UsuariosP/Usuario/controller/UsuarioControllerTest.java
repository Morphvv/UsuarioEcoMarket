package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    private final tools.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();

    private Usuario nuevoUsuario(Long rut, String nombre, String apellido, String email, String telefono, String estadoUsuario){
    Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setEmail(email);
        u.setTelefono("987654321");
        u.setEstadoUsuario(estado);
    return u;
    }

    @Test 
    void crearUsuario() throws Exception{ //Retonrar 200 y el usuario creado xd
        //Given
        Usuario uEntrada = nuevoUsuario(123456789, "Pepito", "Tapia", "pepitoTapia@gmail.com", null);
        Usuario uCreado = nuevoUsuario(123456789, "Pepito", "Tapia", "pepitoTapia@gmail.com", "ACTIVO");
        Usuario invalido = nuevoUsuario(11111111L, "", "correo-malo", "ACTIVO");

        Mockito.when(usuarioService.crear(any(Usuario.class))).thenReturn(uCreado);

        //When y then
        mockMvc.perform(post("/api/v1/usuarios/crear")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entrada)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Pepito"))
        .andExpect(jsonPath("$.estadoUsuario").value("ACTIVO"))
       
        mockMvc.perform(post("/api/v1/usuarios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarUsuarios() throws Exception{ //Retornar igual 200
        //Given
        Usuario u1 = nuevoUsuario(123456789, "Pepito", "PepitoTapia@gmail.com", "ACTIVO");
        Usuario u2 = nuevoUsuario(987654321, "Maria", "Maria@gmail.com". "ACTIVO");
        Mockito.when(usuarioService.listar()).thenReturn(Arrays.asList(u1, u2));

        //When y then 
        mockMvc.perform(get("/api/v1/usuarios/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nombre", is("Pepito")))
            .andExpect(jsonPath("$[1].nombre", is("Maria")));
    }

    @Test
    void buscarUsuario() throws Exception{ //Retonar 200
        //Given
        Usuario uBuscado = nuevoUsuario(123456789, "Pepito", "pepitoTapia@gmail.com", "ACTIVO");
        Mockito.when(usuarioService.buscarPorId(123456789)).thenReturn(uBuscado);

        //When y then
        mockMvc.perfon(get("api/v1/usuarios/Buscar/12345678"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Pepito"))
            .andExpect(jsonPath("$.email").value("pepitoTapia@gmail.com"));
    }

    @Test
    void modificarUsuario() throws Exception{ //200
        //Given
        Usuario uDatos = nuevoUsuario(123456789, "Pepito", "Tapia", "pepitoTapia@gmail.com", "ACTIVO");
        Mockito.when(usuarioService.modificarUsuario(eq(123456789), any(Usuario.class)))
                .thenReturn(uDatos);

        //When y then
        mockMvc.perform(put("/api/v1/usuarios/modificar/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uDatos)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Pepito"));
    }

    @Test
    void desactivarUsuario() throws Exception{ //200
        //Given
        Usuario uDesactivado = nuevoUsuario(123456789, "Pepito", "Tapia", "pepitoTapia@gmail.com", "INACTIVO");
        Mockito.when(usuarioService.desactivar(123456789)).thenReturn(uDesactivado);

        //When y then
        mockMvc.perform(put("/api/v1/usuarios/desactivar/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoUsuario").value("INACTIVO"));
    }

    @Test
    void eliminarUsuario() throws Exception{
        //Given
        Mockito.doNothing().when(usuarioService).eliminar(123456789);

        //When y then
        mockMvc.perfom(delete("api/v1/usuarios/eliminar/123456789"))
                .andExpect(status().isOk());
    }


 
}
