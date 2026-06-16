package com.UsuariosP.Usuario.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sesiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSesion;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El token de sesion es obligatorio")
    private String tokenSesion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaExpiracion;
    private String estadoSesion;
}

