package com.UsuariosP.Usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.UsuariosP.Usuario.exception.GlobalExceptionHandler;
import com.UsuariosP.Usuario.model.CuentaUsuario;
import com.UsuariosP.Usuario.service.CuentaUsuarioService;
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
class CuentaUsuarioControllerTest {

    @Mock
    private CuentaUsuarioService cuentaUsuarioService;

    @InjectMocks
    private CuentaUsuarioController cuentaUsuarioController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cuentaUsuarioController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private CuentaUsuario nuevaCuenta(Long idUsuario, String nombreUsuario, String email, String password, String rol, String estadoCuenta) {
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
    void crearCuenta() throws Exception {
        CuentaUsuario cEntrada = nuevaCuenta(null, "jperez", "jperez@gmail.com", "123456", null, null);
        CuentaUsuario cCreado = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "123456", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.crear(any(CuentaUsuario.class))).thenReturn(cCreado);

        mockMvc.perform(post("/api/v1/cuentaUsuario/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cEntrada)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.rol").value("CLIENTE"))
            .andExpect(jsonPath("$.estadoCuenta").value("ACTIVA"));
    }

    @Test
    void listarCuentaUsuario() throws Exception {
        CuentaUsuario c1 = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE", "ACTIVA");
        CuentaUsuario c2 = nuevaCuenta(2L, "Chamo", "chamo@gmail.com", "arepa", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.listar()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/v1/cuentaUsuario/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreUsuario", is("jperez")));
    }

    @Test
    void modificarCuenta() throws Exception {
        CuentaUsuario cDatos = nuevaCuenta(1L, "JavierA", "javierAlvarez@gmail.com", "exploraNuev0", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.modificarCuentaU(eq(1L), any(CuentaUsuario.class)))
                .thenReturn(cDatos);

        mockMvc.perform(put("/api/v1/cuentaUsuario/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cDatos)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreUsuario").value("JavierA"));
    }

    @Test
    void autenticarCuenta() throws Exception {
        CuentaUsuario cIntento = nuevaCuenta(null, null, "jperez@gmail.com", "1234", null, null);
        CuentaUsuario cAutenticada = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE", "ACTIVA");
        Mockito.when(cuentaUsuarioService.autenticar(any(CuentaUsuario.class))).thenReturn(cAutenticada);

        mockMvc.perform(post("/api/v1/cuentaUsuario/autenticar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cIntento)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreUsuario").value("jperez"))
            .andExpect(jsonPath("$.estadoCuenta").value("ACTIVA"));
    }

    @Test
    void desactivarCuenta() throws Exception {
        CuentaUsuario cDesactivada = nuevaCuenta(1L, "jperez", "jperez@gmail.com", "1234", "CLIENTE", "INACTIVA");
        Mockito.when(cuentaUsuarioService.desactivar(1L)).thenReturn(cDesactivada);

        mockMvc.perform(put("/api/v1/cuentaUsuario/desactivar/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estadoCuenta").value("INACTIVA"));
    }

    @Test
    void eliminarCuenta() throws Exception {
        Mockito.doNothing().when(cuentaUsuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/cuentaUsuario/eliminar/1"))
            .andExpect(status().isNoContent());
    }
}
