package com.UsuariosP.Usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UsuariosP.Usuario.model.CuentaUsuario;

public interface CuentaUsuarioRepository extends JpaRepository<CuentaUsuario, Long> {
    Optional<CuentaUsuario> findByEmail(String email);
    boolean existsByEmail(String email);
}

