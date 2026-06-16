package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.UsuariosP.Usuario.model.Sesion;
import com.UsuariosP.Usuario.service.SesionService;
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
class SesionControllerTest {

    @Mock
    private SesionService sesionService;

    @InjectMocks
    private SesionController sesionController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(sesionController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private Sesion nuevaSesion(Long id, String token, String estado, LocalDateTime fechaExpiracion) {
        Sesion s = new Sesion();
        s.setIdSesion(id);
        s.setIdUsuario(11111111L);
        s.setTokenSesion(token);
        s.setEstadoSesion(estado);
        s.setFechaExpiracion(fechaExpiracion);
        return s;
    }

    @Test
    void iniciarSesion() throws Exception {
        Sesion entradaS = nuevaSesion(null, "token-abv", null, LocalDateTime.now().plusHours(2));
        Sesion iniciadaS = nuevaSesion(1L, "token-abc", "ACTIVA", LocalDateTime.now().plusHours(2));
        Mockito.when(sesionService.iniciarSesion(any(Sesion.class))).thenReturn(iniciadaS);

        mockMvc.perform(post("/api/v1/sesion/iniciar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entradaS)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estadoSesion").value("ACTIVA"))
            .andExpect(jsonPath("$.tokenSesion").value("token-abc"));
    }

    @Test
    void listarSesiones() throws Exception {
        Sesion s1 = nuevaSesion(1L, "token-1", "ACTIVA", LocalDateTime.now().plusHours(1));
        Sesion s2 = nuevaSesion(2L, "token-2", "INACTIVA", LocalDateTime.now().minusHours(1));
        Mockito.when(sesionService.listarS()).thenReturn(Arrays.asList(s1, s2));

        mockMvc.perform(get("/api/v1/sesion/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tokenSesion", is("token-1")));
    }

    @Test
    void modificarSesion() throws Exception {
        Sesion datos = nuevaSesion(1L, "token-nuevo", "ACTIVA", LocalDateTime.now().plusHours(3));
        Mockito.when(sesionService.modificarSesion(eq(1L), any(Sesion.class))).thenReturn(datos);

        mockMvc.perform(put("/api/v1/sesion/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datos)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tokenSesion").value("token-nuevo"));
    }

    @Test
    void validarSesion() throws Exception {
        Sesion valida = nuevaSesion(1L, "token-abc", "ACTIVA", LocalDateTime.now().plusHours(1));
        Mockito.when(sesionService.validarSesion(1L)).thenReturn(valida);

        mockMvc.perform(get("/api/v1/sesion/validar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoSesion").value("ACTIVA"));
    }

    @Test
    void expirarSesion() throws Exception {
        Sesion cerrada = nuevaSesion(1L, "token-abc", "INACTIVA", LocalDateTime.now().plusHours(1));
        Mockito.when(sesionService.cerrarSesion(1L)).thenReturn(cerrada);

        mockMvc.perform(put("/api/v1/sesion/expirar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoSesion").value("INACTIVA"));
    }

    @Test
    void eliminarSesion() throws Exception {
        Mockito.doNothing().when(sesionService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/sesion/eliminar/1"))
                .andExpect(status().isOk());
    }
}
