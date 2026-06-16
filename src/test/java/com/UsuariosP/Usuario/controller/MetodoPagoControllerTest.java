package com.UsuariosP.Usuario.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.service.MetodoPagoService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(MetodoPagoController.class)
@ActiveProfiles("test")
class MetodoPagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetodoPagoService metodoPagoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void crearMetodoPago() throws Exception{ //Retornar 200 y no es el metodo principal
        //Given
        MetodoPago entradaM = nuevoMetodo(0L, "CREDITO", null, null);
        MetodoPago creadoM = nuevoMetodo(1L, "CREDITO", false, true);
        Mockito.when(metodoPagoService.crear(any(MetodoPago.class))).thenReturn(creadoM);

        //When y then
        mockMvc.perform(post("/api/v1/metodoPago/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValuesAsString(entradaM)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activo").value(true))
            .andExpect(jsonPath("$.principal").value(false));
    }

    @Test
    void listarMetodosPagos() throws Exception{
        //Given
        MetodoPago m1 = nuevoMetodo(1L, "CREDITO", false, true);
        MetodoPago m2 = nuevoMetodo(2L, "DEBITO", true, true);
        Mockito.when(metodoPagoService.listar()).thenReturn(Arrays.asList(m1, m2));

        //When y then
        mockMvc.perform(get("api/v1/metodoPago/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipoPago", is("CREDITO")));
    }

    @Test
    void listarPorUsuario() throws Exception{
        //Given
        MetodoPago m1 = nuevoMetodo(1L, "CREDITO", false, true);
        MetodoPago m2 = nuevoMetodo(2L, "DEBITO", true, true);
        Mockito.when(metodoPagoService.listar()).thenReturn(Arrays.asList(m1, m2));

        //When y then
        mockMvc.perform(get("/api/v1/metodoPago/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipoPago", is("CREDITO")));
    }

    @Test
    void modificarMetodoPago() throws Exception{
        //Given
        MetodoPago datos = nuevoMetodo(1L, "DEBITO", false, true);
        Mockito.when(metodoPagoService.modificarMetodoPago(eq(1L), any(MetodoPago.class)))
                .thenReturn(datos);

        //When y then
        mockMvc.perform(put("/api/v1/metodoPago/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writesValueAsString(datos)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tipoPago").value("DEBITO"));
    }

    @Test
    void marcarPrincipal() throws Exception{
        //Given
        MetodoPago principal = nuevoMetodo(1L, "CREDITO", true, true);
        Mockito.when(metodoPagoService.marcarComoPrincipal(1L)).thenReturn(principal);

        //When y then
        mockMvc.perform(put("/api/v1/metodoPago/principal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.principal").value(true));
    }

    @Test
    void desactivarMetodoPago() throws Exception{
        //Given
        MetodoPago desactivadoM = nuevoMetodo(1L, "CREDITO", false, false);
        Mockito.when(metodoPagoService.desactivar(1L)).thenReturn(desactivadoM);

        //When y then
        mockMvc.perform(put("/api/v1/metodoPago/desactivar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void eliminarMetodoPago() throws Exception{
        //Given
        Mockito.doNothing().when(metodoPagoService).eliminar(1L);

        //When y then
        mockMvc.perform(delete("/api/v1/metodoPago/eliminar/1"))
                .andExpect(status().isOk());
    }    
}
