package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.service.MetodoPagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

@ExtendWith(MockitoExtension.class)
class MetodoPagoControllerTest {

    @Mock
    private MetodoPagoService metodoPagoService;

    @InjectMocks
    private MetodoPagoController metodoPagoController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(metodoPagoController).build();
    }

    private MetodoPago nuevoMetodo(long id, String tipoPago, Boolean principal, Boolean activo) {
        MetodoPago m = new MetodoPago();
        m.setIdMetodoPago(id);
        m.setTipoPago(tipoPago);
        m.setProveedorPago("Transbank");
        m.setTokenPago("tok_123");
        m.setUltimosDigitos("4242");
        m.setAlias("Mi tarjeta");
        m.setTitular("Juan Perez");
        m.setActivo(activo);
        m.setPrincipal(principal);
        return m;
    }

    @Test
    void crearMetodoPago() throws Exception {
        MetodoPago entradaM = nuevoMetodo(0L, "CREDITO", null, null);
        MetodoPago creadoM = nuevoMetodo(1L, "CREDITO", false, true);
        Mockito.when(metodoPagoService.crear(any(MetodoPago.class))).thenReturn(creadoM);

        mockMvc.perform(post("/api/v1/metodoPago/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entradaM)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activo").value(true))
            .andExpect(jsonPath("$.principal").value(false));
    }

    @Test
    void listarMetodosPagos() throws Exception {
        MetodoPago m1 = nuevoMetodo(1L, "CREDITO", false, true);
        MetodoPago m2 = nuevoMetodo(2L, "DEBITO", true, true);
        Mockito.when(metodoPagoService.listar()).thenReturn(Arrays.asList(m1, m2));

        mockMvc.perform(get("/api/v1/metodoPago/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipoPago", is("CREDITO")));
    }

    @Test
    void listarPorUsuario() throws Exception {
        MetodoPago m1 = nuevoMetodo(1L, "CREDITO", false, true);
        MetodoPago m2 = nuevoMetodo(2L, "DEBITO", true, true);
        Mockito.when(metodoPagoService.listarPorUsuario(12345678L)).thenReturn(Arrays.asList(m1, m2));

        mockMvc.perform(get("/api/v1/metodoPago/usuario/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipoPago", is("CREDITO")));
    }

    @Test
    void modificarMetodoPago() throws Exception {
        MetodoPago datos = nuevoMetodo(1L, "DEBITO", false, true);
        Mockito.when(metodoPagoService.modificarMetodoPago(eq(1L), any(MetodoPago.class)))
                .thenReturn(datos);

        mockMvc.perform(put("/api/v1/metodoPago/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datos)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipoPago").value("DEBITO"));
    }

    @Test
    void marcarPrincipal() throws Exception {
        MetodoPago principal = nuevoMetodo(1L, "CREDITO", true, true);
        Mockito.when(metodoPagoService.marcarComoPrincipal(1L)).thenReturn(principal);

        mockMvc.perform(put("/api/v1/metodoPago/principal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.principal").value(true));
    }

    @Test
    void desactivarMetodoPago() throws Exception {
        MetodoPago desactivadoM = nuevoMetodo(1L, "CREDITO", false, false);
        Mockito.when(metodoPagoService.desactivar(1L)).thenReturn(desactivadoM);

        mockMvc.perform(put("/api/v1/metodoPago/desactivar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void eliminarMetodoPago() throws Exception {
        Mockito.doNothing().when(metodoPagoService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/metodoPago/eliminar/1"))
                .andExpect(status().isOk());
    }
}
