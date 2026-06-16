package com.UsuariosP.Usuario.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.service.DireccionEnvioService;

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

@WebMvcTest(DireccionEnvioController.class)
@ActiveProfiles("test")
class DireccionEnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionEnvioService direccionEnvioService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DireccionEnvio nuevaDireccion(Long idDireccion, Long idUsuario, String calle, String numero, String comuna, String ciudad, String region, String codigoPostal, String referencia, Boolean direccionPrincipal, Boolean activa){
        DireccionEnvio d = new DireccionEnvio();
        d.setIdDireccion(idDireccion);
        d.setIdUsuario(idUsuario);
        d.setCalle(calle);
        d.setNumero("123");
        d.setComuna("Santiago");
        d.setCiudad("Santiago");
        d.setRegion("RM");
        d.setCodigoPostal("8320000");
        d.setReferencia("Casa azul");
        d.setDireccionPrincipal(principal);
        d.setActiva(activa);
        return d;
    }

    @Test
    void crearDireccion() throws Exception{ // 200 Activa y no principal la direccion
        //Given
        DireccionEnvio dEntrada = nuevaDireccion(null, 11L, "Av. Siempre viva", null, null);
        DireccionEnvio dCreado = nuevaDireccion(1L, 11L, "Av. Siempre viva", false, true);
        Mockito.when(direccionEnvioService.crear(any(DireccionEnvio.class))).thenReturn(dCreado);

        //When y then
        mockMvc.perform(post("/api/v1/direccionEnvio/crear")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValuesAsString(entrada)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.activa").value(true))
        .andExpect(jsonPath("$.direccionPrincipal").value(false));
    }

    @Test 
    void listarDirecciones() throws Exception{ //Reotnar 200 
        //Given 
        DireccionEnvio d1 = nuevaDireccion(1L, 11L, "Calle 1", false, true);
        DireccionEnvio d2 = nuevaDireccion(2L, 22L, "Calle 2", true true);
        Mockito.when(direccionEnvioService.listar()).thenReturn(Arrays.asList(d1, d2));

        //When y then
        mockMvc.perform(get("/api/v1/direccionEnvio/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[$].calle", is("Calle1"));)
    }

    @Test 
    void listarPorUsuario() throws Exception{ //200
        //Given
        DireccionEnvio d1 = nuevaDireccion(1L, 11L, "Calle unica", true, true);
        Mockito.when(direccionEnvioService.listarPorUsuarioD(123456789)).thenReturn(Arrays.asList());

        //When y then
        mockMvc.perform(get("/api/v1/direccionEnvio/usuario/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].calle", is("Calle unica")));
    }


    @Test 
    void modificarDireccion() throws Exception{ //Retornar 200
        //Given
        DireccionEnvio datosD = nuevaDireccion(1L, 11L, "Calle nueva", false, true);
        Mockito.when(direccionEnvioService.modificarDireccionEnvio(eq(1L), any(DireccionEnvio.classs)))
            .thenReturn(datosD);

            //When y then
            mockMvc.perform(put("/api/v1/direccionEnvio/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writesAsString(datosD)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.calle").value("Calle nueva"));
    }

    @Test
    void marcarPrincipal() throws Exception{ //200
        //Given
        DirecionEnvio principalD = nuevaDireccion(1L, 23L, "Calle A", true, true);
        Mockito.when(direccionEnvioService.marcarComoPrincipal(1L)).thenReturn(principalD);

        //When y then
        mockMvc.perform(put("/api/v1/direccionEnvio/principal/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccionPrincipal").value(true));
    }

    @Test 
    void desactivarDireccion() throws Exception{
        //Given
        DireccionEnvio desactivadaD = nuevaDireccion(1L, 23L, "Calle A", false false);
        Mockito.when(direccionEnvioService.desactivar(1L)).thenReturn(desactivadaD);

        //When y then
        mockMvc.perform(put("/api/v1/direccionEnvio/desactivar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activa").value(false));
    }

    @Test
    void eliminarDireccion() throws Exception{
        //Given
        Mockito.doNothing().when(direccionEnvioService).eliminar(1L);

        //When y then
        mockMvc.perform(delete("api/v1/direccionEnvio/eliminar/1"))
            .andExpect(status().isOk());
    }    
}
