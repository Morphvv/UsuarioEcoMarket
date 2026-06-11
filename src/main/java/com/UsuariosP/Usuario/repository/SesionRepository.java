package com.UsuariosP.Usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UsuariosP.Usuario.model.Sesion;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
}

