package com.UsuariosP.Usuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UsuariosP.Usuario.model.DireccionEnvio;

public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {
    List<DireccionEnvio> findByActivaTrue();
    List<DireccionEnvio> findByUsuarioRut(Long rut);
    List<DireccionEnvio> findByUsuarioRutAndActivaTrue(Long rut);
}
