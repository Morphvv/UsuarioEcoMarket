package com.UsuariosP.Usuario.repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.UsuariosP.Usuario.model.DireccionEnvio;
import com.UsuariosP.Usuario.model.Usuario;

@SpringBootTest
@ActiveProfiles("test")
class DireccionEnvioRepositoryIT {

    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario nuevoUsuario(Long rut, String email) {
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre("Admin");
        u.setApellido("Test");
        u.setEmail(email);
        u.setTelefono("987654321");
        u.setFechaRegistro(LocalDateTime.now());
        u.setEstadoUsuario("ACTIVO");
        return u;
    }

    private DireccionEnvio nuevaDireccionEnvio(Usuario usuario, String calle, boolean activa) {
        DireccionEnvio d = new DireccionEnvio();
        d.setUsuario(usuario);
        d.setCalle(calle);
        d.setNumero("123");
        d.setComuna("Santiago");
        d.setCiudad("Santiago");
        d.setRegion("RM");
        d.setCodigoPostal("8320000");
        d.setDireccionPrincipal(false);
        d.setActiva(activa);
        return d;
    }

    @Test
    void buscarUsuarioRut() {
        Usuario propietario = usuarioRepository.save(nuevoUsuario(87654321L, "propietarioPepe@gmail.com"));
        Usuario menor = usuarioRepository.save(nuevoUsuario(12345678L, "floymenor@gmail.com"));

        direccionEnvioRepository.save(nuevaDireccionEnvio(propietario, "Calle A", true));
        direccionEnvioRepository.save(nuevaDireccionEnvio(propietario, "Calle B", false));
        direccionEnvioRepository.save(nuevaDireccionEnvio(menor, "Calle C", true));

        List<DireccionEnvio> direcciones = direccionEnvioRepository.findByUsuarioRut(87654321L);

        assertThat(direcciones).hasSize(2);
        assertThat(direcciones).allMatch(d -> d.getUsuario().getRut().equals(87654321L));
    }

    @Test
    void buscarUsuarioRut_Activa() {
        Usuario propietario = usuarioRepository.save(nuevoUsuario(22334455L, "propietarioJose@gmail.com"));
        direccionEnvioRepository.save(nuevaDireccionEnvio(propietario, "Calle Activa", true));
        direccionEnvioRepository.save(nuevaDireccionEnvio(propietario, "Calle inactiva", false));

        List<DireccionEnvio> activas = direccionEnvioRepository.findByUsuarioRutAndActivaTrue(22334455L);

        assertThat(activas).hasSize(1);
        assertThat(activas.get(0).getCalle()).isEqualTo("Calle Activa");
    }
}
