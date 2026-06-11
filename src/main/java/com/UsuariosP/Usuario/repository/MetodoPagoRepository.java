package com.UsuariosP.Usuario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UsuariosP.Usuario.model.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByPrincipalTrue();
    List<MetodoPago> findByActivoTrue();

    List<MetodoPago> findByUsuarioRut(Long rut);
    List<MetodoPago> findByUsuarioRutAndActivoTrue(Long rut);
}
