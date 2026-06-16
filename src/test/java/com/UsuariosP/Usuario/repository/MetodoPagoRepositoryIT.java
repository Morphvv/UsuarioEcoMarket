package com.UsuariosP.Usuario.repository;
import com.UsuariosP.Usuario.model.MetodoPago;
import com.UsuariosP.Usuario.model.Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MetodoPagoRepositoryIT {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired 
    private UsuarioRepository usuarioRepository;

    private Usuario nuevoUsuario(Long rut, String email){
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre("Propietario");
        u.setApellido("Test");
        u.setEmail(email);
        u.setTelefono("987654321");
        u.setFechaRegistro(LocalDateTime.now());
        u.setEstadoUsuario("ACTIVO");
        return u;
    }

    private MetodoPago nuevoMetodo(Usuario usuario, String tipoPago, boolean Activo){
        MetodoPago m = new MetodoPago();
        m.setUsuario(usuario);   // <- enlaza con el dueno (llena usuario_id)
        m.setTipoPago(tipoPago);
        m.setProveedorPago("Transbank");
        m.setActivo(activo);
        m.setPrincipal(false);
        return m;
    }

    @Test
    void buscarUsuarioRut(){ //Retornar solo los metodos de ese usuario
        Usuario propetario = usuarioRepository.save(nuevoUsuario(66666666L, "pago1@gmail.com"));
        Usuario otro = usuarioRepository.save(nuevoUsuario(77777777L, "pago2@gmail.com"));

        metodoPagoRepository.save(nuevoMetodo(propetario, "CREDITO", true));
        metodoPagoRepository.save(nuevoMetodo(propetario, "DEBITO", false));
        metodoPagoRepository.save(nuevoMetodo(otro, "CREDITO", true));

        List<MetodoPago> metodos = metodoPagoRepository.findByUsuarioRut(66666666L);

        assertThat(metodos).hasSize(2);
        assertThat(metodos).allMatch(m -> m.getUsuario().getRut().equals(66666666L));
    }

    @Test
    void buscarUsuarioRut_activo(){ //Retornar los activos de ese usuario
        Usuario propietario = usuarioRepository.save(nuevoUsuario(88888888L, "pago3@gmail.com"));
        metodoPagoRepository.save(nuevoMetodo(propietario, "CREDITO", true));
        metodoPagoRepository.save(nuevoMetodo(propietario, "DEBITO", false));

        List<MetodoPago> activos = metodoPagoRepository.findByUsuarioRutActivo(88888888L);

        assertThat(activos).hasSize(1);
        assertThat(activos.get(0).getTipoPago()).isEqualTo("CREDITO");
    }
    
}
