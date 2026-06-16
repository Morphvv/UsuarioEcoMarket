package com.UsuariosP.Usuario.repository;

import com.UsuariosP.Usuario.model.CuentaUsuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CuentaUsuarioRepositoryIT {

    @Autowired
    private CuentaUsuarioRepository cuentaUsuarioRepository;

    private CuentaUsuario nuevaCuenta(String nombreUsuario, String email){
        CuentaUsuario c = new CuentaUsuario();

        c.setNombreUsuario(nombreUsuario);
        c.setEmail(email);
        c.setPassword("1234");
        c.setRol("CLIENTE");
        c.setEstadoCuenta("ACTIVA");
        return c;
    }

    @Test
    void guardarYbuscarId(){
        CuentaUsuario cuenta = nuevaCuenta("jperez", "jperez@gmail.com");

        CuentaUsuario cGuardada = cuentaUsuarioRepository.save(cuenta);
        Optional<CuentaUsuario> cEncontrada = cuentaUsuarioRepository.findById(cGuardada.getIdUsuario());

        assertThat(cGuardada.getIdUsuario()).isNotNull();
        assertThat(cEncontrada).isPresent();
        assertThat(cEncontrada.get().getEmail()).isEqualTo("jperez@gmail.com");
    }

    @Test
    void buscarEmail(){ //Cuando existe debe retornar a la cuenta
        cuentaUsuarioRepository.save(nuevaCuenta("amaria", "amaria@gmail.com"));

        Optional <CuentaUsuario> cEncontrada = cuentaUsuarioRepository.findByEmail("amaria@gmail.com");

        assertThat(cEncontrada).isPresent();
        assertThat(cEncontrada.get().getNombreUsuario()).isEqualTo("amaria");
    }

    @Test 
    void  buscarEmail_null(){ //Cuando no existe el correo y retornar vacio
        Optional<CuentaUsuario> cEncontrada = cuentaUsuarioRepository.findByEmail("momo@gmail.com");

        assertThat(cEncontrada).isNotPresent();
    }
}
