package com.UsuariosP.Usuario.repository;
import com.UsuariosP.Usuario.model.Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles
class UsuarioRepositoryIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario nuevoUsuario(Long rut, String email){
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre("Juan");
        u.setApellido("Perez");
        u.setEmail(email);
        u.setTelefono("987654321");
        u.setFechaRegistro(LocalDateTime.now());
        u.setEstadoUsuario("ACTIVO");
        return u;
    }

    @Test
    void buscarEmail(){ //Cuando existe y retornar usuario
        usuarioRepository.save(nuevoUsuario(12345678L, "juan@gmail.com"));

        Optional<Usuario> encontrado= usuarioRepository.findByEmail("juan@gmail.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getRut()).isEqualTo(12345678L);
    }

    @Test
    void buscarEmail_null(){ //Cuando no existe el correo y no retorna nada
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("momo@gmail.com");

        assertThat(encontrado).isNotPresent();
    }

    @Test
    void existsEmail(){
        usuarioRepository.save(nuevoUsuario(23456781L, "jose@gmail.com"));

        assertThat(usuarioRepository.existsByEmail("jose@gmail.com")).isTrue();
        assertThat(usuarioRepository.existsByEmail("raquel@gmail.com")).isFalse();
    }
}
