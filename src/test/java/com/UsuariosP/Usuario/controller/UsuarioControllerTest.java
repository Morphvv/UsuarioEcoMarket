package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

@SuppressWarnings("removal")
@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(usuarioController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private Usuario nuevoUsuario(Long rut, String nombre, String apellido, String email, String telefono, String estadoUsuario) {
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setEmail(email);
        u.setTelefono(telefono);
        u.setEstadoUsuario(estadoUsuario);
        u.setFechaRegistro(LocalDateTime.now());
        return u;
    }

    @Test
    void crearUsuario() throws Exception {
        Usuario uEntrada = nuevoUsuario(123456789L, "Pepito", "Tapia", "pepitoTapia@gmail.com", "5691234567", "ACTIVO");
        Usuario uCreado = nuevoUsuario(123456789L, "Pepito", "Tapia", "pepitoTapia@gmail.com", "5691234567", "ACTIVO");

        Mockito.when(usuarioService.crear(any(Usuario.class))).thenReturn(uCreado);

        mockMvc.perform(post("/api/v1/usuarios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uEntrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pepito"))
                .andExpect(jsonPath("$.estadoUsuario").value("ACTIVO"));
    }

    @Test
    void listarUsuarios() throws Exception {
        Usuario u1 = nuevoUsuario(123456789L, "Pepito", "Tapia", "pepetapia@gmail.com", "5691234567", "ACTIVO");
        Usuario u2 = nuevoUsuario(987654321L, "Maria", "González", "maria@gmail.com", "5691234568", "ACTIVO");
        Mockito.when(usuarioService.listar()).thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/api/v1/usuarios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Pepito")))
                .andExpect(jsonPath("$[1].nombre", is("Maria")));
    }

    @Test
    void buscarUsuario() throws Exception {
        Usuario uBuscado = nuevoUsuario(123456789L, "Pepito", "Tapia", "pepitoTapia@gmail.com", "5691234567", "ACTIVO");
        Mockito.when(usuarioService.buscarPorId(123456789L)).thenReturn(uBuscado);

        mockMvc.perform(get("/api/v1/usuarios/buscar/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pepito"))
                .andExpect(jsonPath("$.email").value("pepitoTapia@gmail.com"));
    }

    @Test
    void modificarUsuario() throws Exception {
        Usuario uDatos = nuevoUsuario(123456789L, "Pepito", "Tapia", "pepitoTapia@gmail.com", "5691234567", "ACTIVO");
        Mockito.when(usuarioService.modificarUsuario(eq(123456789L), any(Usuario.class))).thenReturn(uDatos);

        mockMvc.perform(put("/api/v1/usuarios/modificar/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uDatos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pepito"));
    }

    @Test
    void desactivarUsuario() throws Exception {
        Usuario uDesactivado = nuevoUsuario(123456789L, "Pepito", "Tapia", "pepitoTapia@gmail.com", "5691234567", "INACTIVO");
        Mockito.when(usuarioService.desactivar(123456789L)).thenReturn(uDesactivado);

        mockMvc.perform(put("/api/v1/usuarios/desactivar/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoUsuario").value("INACTIVO"));
    }

    @Test
    void eliminarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminar(123456789L);

        mockMvc.perform(delete("/api/v1/usuarios/eliminar/123456789"))
                .andExpect(status().isOk());
    }
}
