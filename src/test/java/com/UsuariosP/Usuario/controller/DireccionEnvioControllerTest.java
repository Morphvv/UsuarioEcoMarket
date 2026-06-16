package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.model.Usuario;
import com.UsuariosP.Usuario.service.DireccionEnvioService;
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
class DireccionEnvioControllerTest {

    @Mock
    private DireccionEnvioService direccionEnvioService;

    @InjectMocks
    private DireccionEnvioController direccionEnvioController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(direccionEnvioController).build();
    }

    private DireccionEnvio nuevaDireccion(Long idDireccion, Long idUsuario, String calle, String numero, String comuna, String ciudad, String region, String codigoPostal, String referencia, Boolean direccionPrincipal, Boolean activa) {
        DireccionEnvio d = new DireccionEnvio();
        d.setIdDireccion(idDireccion);
        if (idUsuario != null) {
            Usuario usuario = new Usuario();
            usuario.setRut(idUsuario);
            d.setUsuario(usuario);
        }
        d.setCalle(calle);
        d.setNumero(numero);
        d.setComuna(comuna);
        d.setCiudad(ciudad);
        d.setRegion(region);
        d.setCodigoPostal(codigoPostal);
        d.setReferencia(referencia);
        d.setDireccionPrincipal(direccionPrincipal);
        d.setActiva(activa);
        return d;
    }

    @Test
    void crearDireccion() throws Exception {
        DireccionEnvio dEntrada = nuevaDireccion(null, 11L, "Av. Siempre viva", "123", "Santiago", "Santiago", "RM", "8320000", "Casa azul", false, true);
        DireccionEnvio dCreado = nuevaDireccion(1L, 11L, "Av. Siempre viva", "123", "Santiago", "Santiago", "RM", "8320000", "Casa azul", false, true);
        Mockito.when(direccionEnvioService.crear(any(DireccionEnvio.class))).thenReturn(dCreado);

        mockMvc.perform(post("/api/v1/direccionEnvio/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dEntrada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activa").value(true))
            .andExpect(jsonPath("$.direccionPrincipal").value(false));
    }

    @Test
    void listarDirecciones() throws Exception {
        DireccionEnvio d1 = nuevaDireccion(1L, 11L, "Calle 1", "123", "Santiago", "Santiago", "RM", "8320000", "Casa 1", false, true);
        DireccionEnvio d2 = nuevaDireccion(2L, 22L, "Calle 2", "456", "Santiago", "Santiago", "RM", "8320000", "Casa 2", true, true);
        Mockito.when(direccionEnvioService.listar()).thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/api/v1/direccionEnvio/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].calle", is("Calle 1")));
    }

    @Test
    void listarPorUsuario() throws Exception {
        DireccionEnvio d1 = nuevaDireccion(1L, 11L, "Calle unica", "123", "Santiago", "Santiago", "RM", "8320000", "Casa unica", true, true);
        Mockito.when(direccionEnvioService.listarPorUsuario(12345678L)).thenReturn(Arrays.asList(d1));

        mockMvc.perform(get("/api/v1/direccionEnvio/usuario/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].calle", is("Calle unica")));
    }

    @Test
    void modificarDireccion() throws Exception {
        DireccionEnvio datosD = nuevaDireccion(1L, 11L, "Calle nueva", "123", "Santiago", "Santiago", "RM", "8320000", "Casa nueva", false, true);
        Mockito.when(direccionEnvioService.modificarDireccionEnvio(eq(1L), any(DireccionEnvio.class))).thenReturn(datosD);

        mockMvc.perform(put("/api/v1/direccionEnvio/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(datosD)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.calle").value("Calle nueva"));
    }

    @Test
    void marcarPrincipal() throws Exception {
        DireccionEnvio principalD = nuevaDireccion(1L, 23L, "Calle A", "123", "Santiago", "Santiago", "RM", "8320000", "Casa A", true, true);
        Mockito.when(direccionEnvioService.marcarComoPrincipal(1L)).thenReturn(principalD);

        mockMvc.perform(put("/api/v1/direccionEnvio/principal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccionPrincipal").value(true));
    }

    @Test
    void desactivarDireccion() throws Exception {
        DireccionEnvio desactivadaD = nuevaDireccion(1L, 23L, "Calle A", "123", "Santiago", "Santiago", "RM", "8320000", "Casa A", false, false);
        Mockito.when(direccionEnvioService.desactivar(1L)).thenReturn(desactivadaD);

        mockMvc.perform(put("/api/v1/direccionEnvio/desactivar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activa").value(false));
    }

    @Test
    void eliminarDireccion() throws Exception {
        Mockito.doNothing().when(direccionEnvioService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/direccionEnvio/eliminar/1"))
                .andExpect(status().isOk());
    }
}
