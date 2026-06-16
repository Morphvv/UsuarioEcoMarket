package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.service.CuentaUsuarioService;

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

@WebMvcTest(CuentaUsuarioController.class)
@ActiveProfiles("test")
class CuentaUsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CuentaUsuarioService cuentaUsuarioService;

    private CuentaUsuario nuevaCuenta(Long idUsuario, String nombreUsuario, String email, String password, String rol, String estadoCuenta){
        CuentaUsuario c = new CuentaUsuario();
        c.setIdUsuario(idUsuario);
        c.setNombreUsuario(nombreUsuario);
        c.setEmail(email);
        c.setPassword(password);
        c.setRol(rol);
        c.setEstadoCuenta(estadoCuenta);
        return c;
    }

    @Test
    void crearCuenta() throws Exception{ //Retornar 200 y estado por defecto
        //Given
        CuentaUsuario cEntrada = nuevaCuenta(null , "jperez" , "jperez@gmail.com", "1234", null, null);
        CuentaUsuario cCreado = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.crear(any(CuentaUsuario.class))).thenReturn(cCreado);    

        //When y then
        mockMvc.perfom(post("/api/v1/cuentaUsuario/crear")
                    .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cEntrada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rol").value("CLIENTE"))
            .andExpect(jsonPath("$.estadoCuenta").value("ACTIVA"));
    }

    @Test
    void listarCuentaUsuario() throws Exception{
        //Given
        CuentaUsuario c1 = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE", "ACTIVA");
        CuentaUsuario c2 = nuevaCuenta(2L, "Chamo", "chamo@gmail.com", "arepa", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.listar()).thenReturn(Arrays.asList(c1, c2));

        //When y then
        mockMvc.perform(get("/api/v1/cuenaUsuario/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreUsuario", is("jperez")));
    }

    @Test
    void modificarCuenta() throws Exception{
        //Given
        CuentaUsuario cDatos = nuevaCuenta(1L, "JavierA", "javierAlvarez@gmail.com", "exploraNuev0", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.modificarCuentaU(eq(1L), any(CuentaUsuario.class)))
                .thenReturn(cDatos);

        //When y then
        mockMvc.perform(put("/api/v1/cuentaUsuario/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cDatos)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreUsuario").value("JavierA"));
    }

    @Test
    void autenticarCuenta() throws Exception{ //El service confima las credenciales y devuelve la cuenta
        CuentaUsuario cIntento = nuevaCuenta(null, null, "jperez@gmail.com", "1234", null, null);
        CuentaUsuario cAutenticada = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.autenticar(any(CuentaUsuario.class))).thenReturn(cAutenticada);

        //When y then 
        mockMvc.perform(post("/api/v1/cuentaUsuario/autenticar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMaper.writeValueAsString(cIntento)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreUsuario").value("jperez"))
            .andExpect(jsonPath("$.estadoCuenta").value("ACTIVA"));
    }

    @Test
    void desactivarCuenta() throws Exception{ //200 con estado inactiva
        //Given
        CuentaUsuario cDesactivada = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE" , "INACTIVA");
        Mockito.when(cuentaUsuarioService.desactivar(1L)).thenReturn(cDesactivada);

        //When y then
        mockMvc.perform(put("/api/v1/cuentaUsuario/desactivar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estadoCuenta").value("INACTIVA"));
    }

    @Test 
    void eliminarCuenta() throws Exception{
        //Given
        Mockito.doNothing().when(cuentaUsuarioService).eliminar(1L);

        //When y then
        mockMvc.perform(delete("/api/v1/cuentaUsuario/eliminar/1"))
            .andExpect(status().isOk());
    }
}
