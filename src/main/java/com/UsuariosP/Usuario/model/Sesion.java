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
import jakarta.validation.constraints.Size;

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
    @Size(min = 10, message = "El token de sesion debe tener al menos 10 caracteres")
    private String tokenSesion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaExpiracion;
    private String estadoSesion;
}

